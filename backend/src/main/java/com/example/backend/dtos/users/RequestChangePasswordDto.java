package com.example.backend.dtos.users;

import lombok.Data;

@Data
public class RequestChangePasswordDto {
    private String email;
    private String newPassword;
}
