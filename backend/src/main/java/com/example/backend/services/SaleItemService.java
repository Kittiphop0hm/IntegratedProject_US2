package com.example.backend.services;
import com.example.backend.dtos.*;
import com.example.backend.entities.Brand;
import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

//    public List<SaleItem> checkValues(List<SaleItem> saleItems) {
//        for (SaleItem item : saleItems) {
//            checkValue(item);
//        }
//        return saleItems;
//    }

    public SaleItemResponseDto createSaleItem(CreateSaleItemDto createSaleItemDto) {
        createSaleItemDto.setModel(createSaleItemDto.getModel());
        createSaleItemDto.setDescription(createSaleItemDto.getDescription());
        createSaleItemDto.setColor(createSaleItemDto.getColor());

        if (createSaleItemDto.getQuantity()==null||createSaleItemDto.getQuantity() < 0) {
            createSaleItemDto.setQuantity(1);
        }
        Brand brand = brandRepository.findById(createSaleItemDto.getBrand().getId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + createSaleItemDto.getBrand().getId()));
        SaleItem saleItem = modelMapper.map(createSaleItemDto, SaleItem.class);
        saleItem.setBrand(brand);
        SaleItem savedSaleItem = repository.save(saleItem);
        SaleItemResponseDto responseDto = modelMapper.map(savedSaleItem, SaleItemResponseDto.class);
        responseDto.setBrandName(savedSaleItem.getBrand().getName());
        return responseDto;
    }


    public SaleItem updateSaleItem(int id, SaleItem saleItem) {
        saleItem.setId(id);
        return repository.save(saleItem);
    }

    public void deleteSaleItem(Integer id) {
        SaleItem saleItem = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Sale item not found for id: " + id));
        repository.delete(saleItem);
    }


}
