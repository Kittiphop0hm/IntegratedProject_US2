package com.example.backend.dtos.users;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserNationalCardDto {
    private String filename;
    private MultipartFile file;
}
