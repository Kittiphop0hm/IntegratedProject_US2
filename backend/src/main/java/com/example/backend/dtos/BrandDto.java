package com.example.backend.dtos;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;
@Getter
@Setter
public class BrandDto {

    // @NotNull(message = "Brand ID is required")
    private Integer id;

    // @NotBlank(message = "brand name is required")
    private String name;



}
