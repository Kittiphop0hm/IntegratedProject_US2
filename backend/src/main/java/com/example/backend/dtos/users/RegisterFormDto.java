package com.example.backend.dtos.users;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegisterFormDto {
    private String nickname;
    private String email;
    @Min(8)
    private String password;
    @Min(4)
    @Max(40)
    private String fullname;
    private String role;
    private String mobile;
    private Integer bankAccountNumber;
    private Integer nationalCardNumber;
    private List<MultipartFile> files;
}
