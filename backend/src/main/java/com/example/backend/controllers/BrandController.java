package com.example.backend.controllers;

import com.example.backend.dtos.BrandDto;
import com.example.backend.entities.Brand;
import com.example.backend.services.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/v1/brands")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        List<BrandDto> brandDtoList = brands.stream().map(brand -> modelMapper.map(brand, BrandDto.class)).toList();
        return ResponseEntity.ok(brandDtoList);
    }
}
