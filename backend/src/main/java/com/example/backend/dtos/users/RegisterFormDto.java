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

    public void setNickName(String nickName) {
        if (nickName != null) {
            nickName = nickName.trim();
            this.nickName = nickName.isEmpty() ? null : nickName;
        } else {
            this.nickName = null;
        }
    }

    public void setEmail(String email) {
        if (email != null) {
            email = email.trim();
            this.email = email.isEmpty() ? null : email;
        } else {
            this.email = null;
        }
    }

    public void setPassword(String password) {
        if (password != null) {
            password = password.trim();
            this.password = password.isEmpty() ? null : password;
        } else {
            this.password = null;
        }
    }

    public void setFullName(String fullName) {
        if (fullName != null) {
            fullName = fullName.trim();
            this.fullName = fullName.isEmpty() ? null : fullName;
        } else {
            this.fullName = null;
        }
    }

    public void setUserType(String userType) {
        if (userType != null) {
            userType = userType.trim();
            this.userType = userType.isEmpty() ? null : userType;
        } else {
            this.userType = null;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.trim();
            this.phoneNumber = phoneNumber.isEmpty() ? null : phoneNumber;
        } else {
            this.phoneNumber = null;
        }
    }

    public void setBankAccount(String bankAccount) {
        if (bankAccount != null) {
            bankAccount = bankAccount.trim();
            this.bankAccount = bankAccount.isEmpty() ? null : bankAccount;
        } else {
            this.bankAccount = null;
        }
    }

    public void setBankName(String bankName) {
        if (bankName != null) {
            bankName = bankName.trim();
            this.bankName = bankName.isEmpty() ? null : bankName;
        } else {
            this.bankName = null;
        }
    }

    public void setCardNumber(String cardNumber) {
        if (cardNumber != null) {
            cardNumber = cardNumber.trim();
            this.cardNumber = cardNumber.isEmpty() ? null : cardNumber;
        } else {
            this.cardNumber = null;
        }
    }
}
