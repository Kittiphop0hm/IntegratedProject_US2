package com.example.backend.services;

import com.example.backend.dtos.BrandDto;
import com.example.backend.entities.Brand;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private Repositories repositories;

    public List<Brand> getAllBrands() {
        return brandRepository.findAllByOrderByNameAsc();
    }

    public BrandDto getBrandDtoById(int id) {
        Brand brand = getBrandById(id);
        return modelMapper.map(brand, BrandDto.class);
    }

    public Brand getBrandById(int id) {
        return brandRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + id));
    }
}
