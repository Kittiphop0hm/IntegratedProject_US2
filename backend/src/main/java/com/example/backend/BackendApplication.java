package com.example.backend;

import com.example.backend.dtos.UpdateSaleItemDto;
import com.example.backend.entities.SaleItem;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(UpdateSaleItemDto.class, SaleItem.class)
                .addMappings(m -> m.skip(SaleItem::setId)); // ข้ามการแมป id
        return mapper;
    }
}
