package com.example.backend.services.users;

import com.example.backend.dtos.users.*;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserFileService userFileService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;


    public ResponseUserDto findByEmail(String email) {
        User user = repository.findUserByEmail(email);
        return modelMapper.map(user, ResponseUserDto.class);
    }

    public UserProfileResponseDto findById(Integer id) {
        User user = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User id: " + id + " not found"));
        if (!user.getIsActive()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User id: " + id + " is not active");
        return modelMapper.map(user, UserProfileResponseDto.class);
    }

    @Transactional
    public ResponseUserDto createUser(RegisterFormDto userForm, MultipartFile cardImageFront, MultipartFile cardImageBack) {

        try {
            if (userForm.getUserType().equalsIgnoreCase("SELLER")) {
                // ตรวจสอบอีเมลซ้ำ
                if (repository.existsUserByEmail(userForm.getEmail())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email: " + userForm.getEmail() + " is exists.");
                }

                // สร้าง User object
                User user = modelMapper.map(userForm, User.class);
                user.setPassword(encodePassword(userForm.getPassword()));
                user.setIsActive(false);
                user.setUserType(user.getUserType().toUpperCase());

                // บันทึก user และ commit transaction ทันที
                User addUser = repository.saveAndFlush(user); // ใช้ saveAndFlush()

                // Debug logging
                System.out.println("กำลังจะบันทึกไฟล์สำหรับ user ID: " + addUser.getId());
                System.out.println("User มีอยู่ในฐานข้อมูล: " + repository.existsById(addUser.getId()));

                // ตอนนี้ค่อยเก็บไฟล์
                userFileService.store(cardImageFront, addUser.getId(), "FRONT");
                userFileService.store(cardImageBack, addUser.getId(), "BACK");

                // Refresh หลังจากเก็บไฟล์เสร็จแล้ว
                entityManager.refresh(addUser);

                // ดึงข้อมูล user อีกครั้งเพื่อสร้าง token (ใช้ method เดิมสำหรับ email verification)
                User checkUser = repository.findUserByEmail(userForm.getEmail());
                String token = jwtService.generateJwtToken(checkUser.getId(), checkUser.getEmail());
                emailService.sendEmail(userForm.getEmail(), token);

                return modelMapper.map(addUser, ResponseUserDto.class);

            } else {
                // กรณี UserType ไม่ใช่ SELLER
                User user = modelMapper.map(userForm, User.class);
                user.setIsActive(false);
                user.setPassword(encodePassword(userForm.getPassword()));
                user.setUserType(user.getUserType().toUpperCase());

                // บันทึก user
                User addUser = repository.saveAndFlush(user); // ใช้ saveAndFlush()

                // ตรวจสอบ ID
                if (addUser.getId() == null) {
                    throw new RuntimeException("User ID is null after save operation");
                }

                entityManager.refresh(addUser);

                // ดึงข้อมูล user อีกครั้งเพื่อสร้าง token (ใช้ method เดิมสำหรับ email verification)
                User checkUser = repository.findUserByEmail(userForm.getEmail());
                String token = jwtService.generateJwtToken(checkUser.getId(), checkUser.getEmail());
                emailService.sendEmail(userForm.getEmail(), token);

                return modelMapper.map(addUser, ResponseUserDto.class);
            }

        } catch (DataIntegrityViolationException e) {
            System.err.println("Database constraint violation: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ไม่สามารถสร้างผู้ใช้ได้ เนื่องจากข้อมูลไม่ถูกต้อง");
        } catch (ResponseStatusException e) {
            // Re-throw ResponseStatusException ที่เรา throw เอง
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error during user creation: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "เกิดข้อผิดพลาดในระบบ");
        }
    }

    public ResponseUserDto verifyEmail(Integer userId , String email){
        User user = repository.findUserByEmail(email);
        System.out.println(userId);
        System.out.println(email);
        System.out.println(user.getEmail());
        if(user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if(user.getId().equals(userId)){
            user.setIsActive(true);
            repository.save(user);
            return modelMapper.map(user, ResponseUserDto.class);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification token");
    }

    public String encodePassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public boolean checkPassword(String raw , String encoded){
        return passwordEncoder.matches(raw, encoded);
    }


    public ResponseTokenDto checkLogin(String email, String rawPassword, HttpServletResponse response) {
        User user = repository.findUserByEmail(email);

        if (user == null || !checkPassword(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or Password is incorrect.");
        }
        if (!user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You need to activate your account before signing in.");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .secure(false)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        ResponseTokenDto tokenDto = new ResponseTokenDto();
        tokenDto.setAccessToken(accessToken);
        return tokenDto;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
        for (int i = 0; i < cookies.length; i++) {
            if ("refresh_token".equalsIgnoreCase(cookies[i].getName())) {
                Claims claims = jwtService.extractClaims(cookies[i].getValue());
                User user = repository.findUserByEmail(claims.getSubject());
                if (!repository.existsUserByEmail(user.getEmail())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User email:" + user.getEmail() + " not found");
                if (!user.getIsActive()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active");
            }
        }
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .secure(false)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public UserProfileResponseDto updateUser(Integer id, UserUpdateFormatDto userFormat) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id: " + id + " not exists");
        User user = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User id: " + id + " not found"));
        modelMapper.map(userFormat, user);
        User updatedUser = repository.save(user);
        return modelMapper.map(updatedUser, UserProfileResponseDto.class);
    }

    public ResponseTokenDto refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No refresh token provided");
        }

        try {
            System.out.println("refresh: " + refreshToken);
            Claims claims = jwtService.extractClaims(refreshToken);
            System.out.println(claims);
            String email = claims.getSubject();
            User user = repository.findUserByEmail(email);

            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
            }
            if (!user.getIsActive()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not active");
            }

            String newAccessToken = jwtService.generateAccessToken(user);
            System.out.println("new AccessToken: " + newAccessToken);
            ResponseTokenDto tokenDto = new ResponseTokenDto();
            tokenDto.setAccessToken(newAccessToken);
            return tokenDto;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token");
        }
    }
}
