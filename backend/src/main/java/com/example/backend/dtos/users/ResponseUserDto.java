package com.example.backend.dtos.users;

import lombok.Data;

import java.time.Instant;

@Data
public class ResponseUserDto {
    private Integer id;
    private String nickname;
    private String email;
    private String password;
    private String fullname;
    private String role;
    private String mobile;
    private Integer bankAccountNumber;
    private Integer nationalCardNumber;
    private Instant createdOn;
    private Instant updatedOn;
}
