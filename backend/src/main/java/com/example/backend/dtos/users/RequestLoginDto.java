package com.example.backend.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLoginDto {
    @Email
    @NotEmpty
    @NotNull
    @Size(max = 50)
    private String email;
    @NotEmpty
    @NotNull
    @Size(max = 14)
    private String password;
}
