package com.example.backend.dtos.users;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegisterFormDto {
    private String nickname;
    private String email;
    private String password;
    private String fullname;
    private String role;
    private String mobile;
    private Integer bankAccountNumber;
    private Integer nationalCardNumber;
    private List<MultipartFile> files;
}
