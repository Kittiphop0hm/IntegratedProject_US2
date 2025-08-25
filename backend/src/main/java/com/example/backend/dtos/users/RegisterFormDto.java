package com.example.backend.dtos.users;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegisterFormDto {
    @NotNull
    private String nickName;
    @NotNull
    private String email;
    @NotNull
    @Min(8)
    private String password;
    @NotNull
    @Min(4)
    @Max(40)
    private String fullName;
    @NotNull
    private String userType;
    private String phoneNumber;
    private String bankAccount;
    private String bankName;
    private String idCardNumber;
}
