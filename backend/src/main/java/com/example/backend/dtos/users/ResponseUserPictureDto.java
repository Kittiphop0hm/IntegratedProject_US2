package com.example.backend.dtos.users;

import lombok.Data;

@Data
public class ResponseUserPictureDto {
    private Integer id;
    private String filename;
    private Integer imageViewOrder;
    private Integer usersId;
}
