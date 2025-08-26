package com.example.backend.services.users;

import com.example.backend.dtos.users.RegisterFormDto;
import com.example.backend.dtos.users.ResponseUserDto;
import com.example.backend.entities.User;
import com.example.backend.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public ResponseUserDto findByEmail(String email) {
        User user = repository.findUserByEmail(email);
        return modelMapper.map(user, ResponseUserDto.class);
    }

    @Transactional
    public ResponseUserDto createUser(RegisterFormDto userForm, MultipartFile cardImageFront, MultipartFile cardImageBack) {

        if (userForm.getUserType().toUpperCase().equals("SELLER")) {
            // ตรวจสอบอีเมลซ้ำ
            if (repository.existsUserByEmail(userForm.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email: " + userForm.getEmail() + " is exists.");
            }
            User user = modelMapper.map(userForm, User.class);
            user.setIsActive(false);
            user.setUserType(user.getUserType().toUpperCase());

            // บันทึก user และให้แน่ใจว่าถูก commit แล้ว
            User addUser = repository.save(user);
            repository.flush(); // ใช้ repository.flush() แทน entityManager.flush()

            // ตรวจสอบว่า user ถูกบันทึกพร้อม ID ที่ถูกต้องแล้ว
            if (addUser.getId() == null) {
                throw new RuntimeException("User ID is null after save operation");
            }

            // Debug logging (สามารถลบออกได้หลังจากแก้ไขแล้ว)
            System.out.println("กำลังจะบันทึกไฟล์สำหรับ user ID: " + addUser.getId());
            System.out.println("User มีอยู่ในฐานข้อมูล: " + repository.existsById(addUser.getId()));

            try {
                // ตอนนี้ค่อยเก็บไฟล์
                userFileService.store(cardImageFront, addUser.getId(), "FRONT");
                userFileService.store(cardImageBack, addUser.getId(), "BACK");
            } catch (Exception e) {
                System.err.println("Error storing files for user ID: " + addUser.getId());
                e.printStackTrace();
                throw e; // Re-throw เพื่อให้ transaction rollback
            }

            // Refresh เพื่อให้แน่ใจว่าได้ข้อมูลล่าสุด
            entityManager.refresh(addUser);

            // ดึงข้อมูล user อีกครั้งเพื่อสร้าง token
            User checkUser = repository.findUserByEmail(userForm.getEmail());
            String token = jwtService.generateJwtToken(checkUser.getId(), checkUser.getEmail());
            emailService.sendEmail(userForm.getEmail(), token);

            return modelMapper.map(addUser, ResponseUserDto.class);

        } else {
            // กรณี UserType ไม่ใช่ SELLER
            User user = modelMapper.map(userForm, User.class);
            user.setIsActive(false);
            user.setUserType(user.getUserType().toUpperCase());

            // บันทึก user
            User addUser = repository.save(user);
            repository.flush(); // ใช้ repository.flush()

            // ตรวจสอบ ID
            if (addUser.getId() == null) {
                throw new RuntimeException("User ID is null after save operation");
            }

            entityManager.refresh(addUser);

            // ดึงข้อมูล user อีกครั้งเพื่อสร้าง token
            User checkUser = repository.findUserByEmail(userForm.getEmail());
            String token = jwtService.generateJwtToken(checkUser.getId(), checkUser.getEmail());
            emailService.sendEmail(userForm.getEmail(), token);

            return modelMapper.map(addUser, ResponseUserDto.class);
        }
    }

    public ResponseUserDto verifyEmail(Integer userId , String email){
        User user = repository.findUserByEmail(email);
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
}
