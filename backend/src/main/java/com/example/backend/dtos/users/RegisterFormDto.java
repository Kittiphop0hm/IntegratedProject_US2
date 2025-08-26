package com.example.backend.dtos.users;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private String password;
    @NotNull
    private String fullName;
    @NotNull
    private String userType;
    private String phoneNumber;
    private String bankAccount;
    private String bankName;
    private String cardNumber;
}
