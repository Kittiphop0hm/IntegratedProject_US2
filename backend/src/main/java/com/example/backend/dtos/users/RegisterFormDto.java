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
    private String nickname;
    @NotNull
    private String email;
    @NotNull
    @Min(8)
    private String password;
    @NotNull
    @Min(4)
    @Max(40)
    private String fullname;
    @NotNull
    private String role;
    private String mobile;
    private Integer bankAccountNumber;
    private Integer nationalCardNumber;
    private List<MultipartFile> files;
}
