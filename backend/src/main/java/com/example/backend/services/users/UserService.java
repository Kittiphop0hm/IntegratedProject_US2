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
            if (repository.existsUserByEmail(userForm.getEmail())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email: " + userForm.getEmail() + " is exists.");
            User user = modelMapper.map(userForm, User.class);
            user.setIsActive(false);
            user.setUserType(user.getUserType().toUpperCase());
            User addUser = repository.save(user);
            userFileService.store(cardImageFront, addUser.getId(), "FRONT");
            userFileService.store(cardImageBack, addUser.getId(), "BACK");
            entityManager.refresh(addUser);
            User checkUser = repository.findUserByEmail(userForm.getEmail());
            String token = jwtService.generateJwtToken(checkUser.getId() , checkUser.getEmail());
            emailService.sendEmail(userForm.getEmail(), token);
            return modelMapper.map(addUser, ResponseUserDto.class);
        } else {
            User user = modelMapper.map(userForm, User.class);
            user.setIsActive(false);
            user.setUserType(user.getUserType().toUpperCase());
            User addUser = repository.save(user);
            entityManager.refresh(addUser);
            User checkUser = repository.findUserByEmail(userForm.getEmail());
            String token = jwtService.generateJwtToken(checkUser.getId() , checkUser.getEmail());
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
