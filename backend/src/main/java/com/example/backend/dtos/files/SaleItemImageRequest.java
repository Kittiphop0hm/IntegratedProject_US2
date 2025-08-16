package com.example.backend.dtos.files;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
public class SaleItemImageRequest {
    private Integer order;
    private String fileName;
    private String status;
    private MultipartFile imageFile;
}
