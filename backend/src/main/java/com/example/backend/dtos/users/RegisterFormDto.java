package com.example.backend.dtos.users;

import com.example.backend.validation.ValidSellerFields;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@ValidSellerFields
@Data
public class RegisterFormDto {
    @NotBlank(message = "nickName must not be blank")
    private String nickName;

    @NotBlank(message = "email must not be blank")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "password must not be blank")
    @Size(min = 6, message = "password must be at least 6 characters")
    private String password;

    @NotBlank(message = "fullName must not be blank")
    private String fullName;

    @NotBlank(message = "userType must not be blank")
    private String userType;

    // conditional: เอา annotation บังคับออก ให้ validator ระดับ class ตรวจ
    private String phoneNumber;
    private String bankAccount;
    private String bankName;
    private String cardNumber;
}
