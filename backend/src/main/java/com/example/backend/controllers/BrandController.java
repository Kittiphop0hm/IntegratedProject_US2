package com.example.backend.controllers;

import com.example.backend.dtos.brands.AddUpdateBrandDto;
import com.example.backend.dtos.brands.GetBrandDto;
import com.example.backend.dtos.brands.ResponseBrandsDto;
import com.example.backend.dtos.brands.ListBrandsDto;
import com.example.backend.services.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/v1/brands")
@CrossOrigin(origins = "${app.cors.allowed-origins}", allowCredentials = "true")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<List<ListBrandsDto>> getAllBrands() {
        List<ListBrandsDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBrandDto> getBrandById(@PathVariable Integer id) {
        GetBrandDto brands = brandService.getBrandById(id);
        return ResponseEntity.ok(brands);
    }

    @PostMapping("")
    public ResponseEntity<ResponseBrandsDto> addBrand(@RequestBody AddUpdateBrandDto newBrandDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                brandService.createBrand(newBrandDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBrandsDto> updateBrand(@PathVariable Integer id ,@RequestBody AddUpdateBrandDto newBrandDto) {
        return ResponseEntity.ok(
                brandService.updateBrand(id,newBrandDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseBrandsDto> deleteBrand(@PathVariable Integer id ) {
        brandService.deleteฺBrand(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
