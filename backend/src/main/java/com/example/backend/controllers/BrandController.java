package com.example.backend.controllers;

import com.example.backend.dtos.BrandDto;
import com.example.backend.entities.Brand;
import com.example.backend.services.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(brandService.findAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> getBrandById(@PathVariable Integer id) {
        return ResponseEntity.ok(brandService.findBrandById(id));
    }
}
