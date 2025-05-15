package com.example.backend.services;

import com.example.backend.dtos.brands.AddUpdateBrandDto;
import com.example.backend.dtos.brands.ResponseBrandsDto;
import com.example.backend.dtos.brands.ListBrandsDto;
import com.example.backend.entities.Brand;
import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemRepository;
import com.example.backend.utils.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SaleItemRepository saleItemRepository;

    public List<ListBrandsDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAllByOrderByNameAsc();
        return listMapper.mapList(brands, ListBrandsDto.class ,modelMapper);
    }

    public ResponseBrandsDto getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + id));
        return modelMapper.map(brand, ResponseBrandsDto.class);
    }
    

    public ResponseBrandsDto createBrand(AddUpdateBrandDto newBrandDto) {
        if (brandRepository.existsByName(newBrandDto.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already exists");
        }
        Brand brand = brandRepository.save(modelMapper.map(newBrandDto, Brand.class));
        return modelMapper.map(brand, ResponseBrandsDto.class);
    }

    public ResponseBrandsDto updateBrand(Integer id, AddUpdateBrandDto updateBrandDto) {
//        if (brandRepository.existsByName(updateBrandDto.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is already exists");
//        }
        if (!brandRepository.existsById(id)) {
            throw new ItemNotFoundException("Brand not found for this id :: " + id);
        }
        Brand brand = brandRepository.save(modelMapper.map(updateBrandDto, Brand.class));
        return  modelMapper.map(brand, ResponseBrandsDto.class);
    }

    public void deleteฺBrand(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + id));
        if(!brand.getSaleItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Brand has sale item(s)");
        }
        brandRepository.delete(brand);
    }
}
