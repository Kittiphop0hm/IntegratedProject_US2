package com.example.backend.services;
import com.example.backend.dtos.*;
import com.example.backend.entities.Brand;
import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemRepository;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;


@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private EntityManager entityManager;

    public List<ListItemsDto> findAll() {
        List<SaleItem> saleItems = repository.findAll();
        return saleItems.stream().map(item -> modelMapper.map(item, ListItemsDto.class)).toList();
    }

    public GetItemDto findById(int id) {
        SaleItem saleItem = repository.findById(id).orElseThrow(() -> new ItemNotFoundException("SaleItem not found for this id :: " + id));
        return modelMapper.map(saleItem, GetItemDto.class);
    }



//    public SaleItem checkValue(SaleItem saleItem) {
//        if (saleItem.getColor().contains("null")) {
//            saleItem.setColor(null);
//        } if (saleItem.getRamGb() == 0) {
//            saleItem.setRamGb(null);
//        } if (saleItem.getStorageGb() == 0) {
//            saleItem.setStorageGb(null);
//        } if (saleItem.getScreenSizeInch().doubleValue() <= 0) {
//            saleItem.setScreenSizeInch(null);
//        }
//        return saleItem;
//    }
//
//    public List<SaleItem> checkValues(List<SaleItem> saleItems) {
//        for (SaleItem item : saleItems) {
//            checkValue(item);
//        }
//        return saleItems;
//    }

    @Transactional
    public SaleItemResponseDto createSaleItem(CreateSaleItemDto createSaleItemDto) {
        if (createSaleItemDto.getModel() == null || createSaleItemDto.getDescription() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Model and description cannot be null");
        }
        if (createSaleItemDto.getQuantity() == null || createSaleItemDto.getQuantity() < 0) {
            createSaleItemDto.setQuantity(1);
        }
        createSaleItemDto.setModel(createSaleItemDto.getModel().trim());
        createSaleItemDto.setDescription(createSaleItemDto.getDescription().trim());
        createSaleItemDto.setColor(createSaleItemDto.getColor().trim());
        Brand brand = brandRepository.findById(createSaleItemDto.getBrandId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + createSaleItemDto.getBrandId()));
        SaleItem saleItem = modelMapper.map(createSaleItemDto, SaleItem.class);
        saleItem.setBrand(brand);
        SaleItem savedSaleItem = repository.save(saleItem);
        entityManager.refresh(savedSaleItem);
        SaleItemResponseDto responseDto = modelMapper.map(savedSaleItem, SaleItemResponseDto.class);
        responseDto.setBrandName(savedSaleItem.getBrand().getName());
        return responseDto;
    }

    public SaleItemResponseDto updateSaleItem(Integer id, UpdateSaleItemDto dto) {
        SaleItem existing = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Sale item not found for id: " + id));
        if (dto.getBrandId() != null) {
            Brand brand = brandRepository.findById(dto.getBrandId())
                    .orElseThrow(() -> new ItemNotFoundException("Brand not found for id: " + dto.getBrandId()));
            existing.setBrand(brand);
        }
        modelMapper.map(dto, existing);
        if (existing.getRamGb() != null && existing.getRamGb() == 0) {
            existing.setRamGb(null);
        }
        if (existing.getStorageGb() != null && existing.getStorageGb() == 0) {
            existing.setStorageGb(null);
        }
        if (existing.getScreenSizeInch() != null &&
                existing.getScreenSizeInch().compareTo(BigDecimal.ZERO) <= 0) {
            existing.setScreenSizeInch(null);
        }
        if (existing.getColor() != null && existing.getColor().toLowerCase().contains("null")) {
            existing.setColor(null);
        }
        SaleItem updated = repository.save(existing);
        SaleItemResponseDto response = modelMapper.map(existing, SaleItemResponseDto.class);
        response.setBrandName(updated.getBrand().getName());
        return response;
    }

    public void deleteSaleItem(Integer id) {
        SaleItem saleItem = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Sale item not found for id: " + id));
        repository.delete(saleItem);
    }
}
