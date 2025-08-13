package com.example.backend.dtos.pictures;

import lombok.Data;

@Data
public class ResponsePictureDto {
    private Integer id;
    private String fileName;
    private Integer imageViewOrder;
    private Integer salesId;
}
