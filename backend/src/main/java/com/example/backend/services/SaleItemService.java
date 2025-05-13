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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    private BrandService brandService;
    @Autowired
    private EntityManager entityManager;

    public List<ListItemsDto> findAll() {
        List<SaleItem> saleItems = repository.findAll();
        return saleItems.stream().map(item -> modelMapper.map(item, ListItemsDto.class)).toList();
    }

    public GetItemDto findById(int id) {
        SaleItem saleItem = getSaleItemById(id);
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

    @Transactional
    public SaleItemResponseDto createSaleItem(AddUpdateSaleItemDto createSaleItemDto) {
        Brand brand = brandRepository.findById(createSaleItemDto.getBrand().getId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + createSaleItemDto.getBrand().getId()));
        SaleItem saleItem = modelMapper.map(createSaleItemDto, SaleItem.class);
        saleItem.setBrand(brand);
        SaleItem savedSaleItem = repository.save(saleItem);
        entityManager.refresh(savedSaleItem);
        SaleItemResponseDto responseDto = modelMapper.map(savedSaleItem, SaleItemResponseDto.class);
        responseDto.setBrandName(savedSaleItem.getBrand().getName());
        return responseDto;
    }

    @Transactional
    public SaleItemResponseDto updateSaleItem(int id, AddUpdateSaleItemDto updateSaleItemDto) {
        SaleItem existing = getSaleItemById(id);

        if (updateSaleItemDto.getBrand() == null && updateSaleItemDto.getBrand().getId() == null) {
            throw new ItemNotFoundException("Brand not found for this id :: " + id);
        }
        Brand brand = brandRepository.findById(updateSaleItemDto.getBrand().getId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found with id: " +
                        updateSaleItemDto.getBrand().getId()));

        existing.setBrand(brand);
        existing.setModel(updateSaleItemDto.getModel());
        existing.setDescription(updateSaleItemDto.getDescription());
        existing.setPrice(updateSaleItemDto.getPrice());
        existing.setQuantity(updateSaleItemDto.getQuantity());
        existing.setRamGb(updateSaleItemDto.getRamGb());
        existing.setScreenSizeInch(updateSaleItemDto.getScreenSizeInch());
        existing.setStorageGb(updateSaleItemDto.getStorageGb());
        existing.setColor(updateSaleItemDto.getColor());

        SaleItem updated = repository.save(existing);
        SaleItemResponseDto responseDto = modelMapper.map(updated, SaleItemResponseDto.class);
        responseDto.setBrandName(brand.getName());
        return responseDto;
    }


    public void deleteSaleItem(Integer id) {
        SaleItem saleItem = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Sale item not found for id: " + id));
        repository.delete(saleItem);
    }

    public SaleItem getSaleItemById(int id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("SaleItem not found for this id :: " + id));
    }
}
