package com.example.backend.dtos.users;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @NotEmpty(message = "Current password is required")
    @NotNull
    private String currentPassword;

    @NotEmpty(message = "New password is required")
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    @NotEmpty(message = "Confirm password is required")
    @NotNull
    private String confirmPassword;
}