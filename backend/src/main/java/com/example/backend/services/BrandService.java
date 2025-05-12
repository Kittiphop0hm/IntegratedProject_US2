package com.example.backend.services;

import com.example.backend.dtos.BrandDto;
import com.example.backend.entities.Brand;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BrandRepository brandRepository;

    public List<BrandDto> findAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandDto> brandDto = brands.stream().map(brand -> modelMapper.map(brand, BrandDto.class)).toList();
        return brandDto;
    }

    public BrandDto findBrandById(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + id));
        return modelMapper.map(brand, BrandDto.class);
    }
}
