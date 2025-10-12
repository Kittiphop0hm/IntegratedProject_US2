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
    // t
    @NotNull(message = "email must not be null")
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String fullName;
    // t
    @NotNull(message = "userType must not be null")
    private String userType;
    // t
    @NotNull(message = "phoneNumber must not be null")
    private String phoneNumber;
    // t
    @NotNull(message = "bankAccount must not be null")
    private String bankAccount;
    private String bankName;
    // t
    @NotNull(message = "cardNumber must not be null")
    private String cardNumber;
}
