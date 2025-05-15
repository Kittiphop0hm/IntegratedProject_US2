package com.example.backend.dtos.brands;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ListBrandsDto {
    private Integer id;
    private String name;
    private String websiteUrl;
    private Boolean isActive;
    private String countryOfOrigin;
    private Instant createdOn;
    private Instant updatedOn;
}
