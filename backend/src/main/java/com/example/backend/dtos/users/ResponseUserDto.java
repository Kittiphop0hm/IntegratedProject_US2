package com.example.backend.dtos.users;

import com.example.backend.dtos.files.ListFilesDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

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
