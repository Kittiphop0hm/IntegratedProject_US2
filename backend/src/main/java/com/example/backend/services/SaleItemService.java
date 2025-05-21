package com.example.backend.services;
import com.example.backend.dtos.saleItems.AddUpdateSaleItemDto;
import com.example.backend.dtos.saleItems.GetSaleItemDto;
import com.example.backend.dtos.saleItems.ListSaleItemsDto;
import com.example.backend.dtos.saleItems.ResponseSaleItemsDto;
import com.example.backend.entities.Brand;
import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemRepository;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import java.math.BigDecimal;
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
    private EntityManager entityManager;

    public List<ListSaleItemsDto> findAll() {
        List<SaleItem> saleItems = repository.findAllByOrderByCreatedOn();
        return saleItems.stream().map(item -> modelMapper.map(item, ListSaleItemsDto.class)).toList();
    }

    public GetSaleItemDto findById(int id) {
        SaleItem saleItem = getSaleItemById(id);
        return modelMapper.map(saleItem, GetSaleItemDto.class);
    }


    @Transactional
    public ResponseSaleItemsDto createSaleItem(AddUpdateSaleItemDto createSaleItemDto) {
        brandRepository.findById(createSaleItemDto.getBrand().getId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + createSaleItemDto.getBrand().getId()));
        SaleItem saleItem = modelMapper.map(createSaleItemDto, SaleItem.class);
        SaleItem savedSaleItem = repository.save(saleItem);
        entityManager.refresh(savedSaleItem);
        ResponseSaleItemsDto responseDto = modelMapper.map(savedSaleItem, ResponseSaleItemsDto.class);
        responseDto.setBrandName(savedSaleItem.getBrand().getName());
        return responseDto;
    }

    public ResponseSaleItemsDto updateSaleItem(int id, AddUpdateSaleItemDto updateSaleItemDto) {
        SaleItem existing = getSaleItemById(id);
        if (updateSaleItemDto.getBrand() == null && updateSaleItemDto.getBrand().getId() == null) {
            throw new ItemNotFoundException("Brand not found for this id :: " + id);
        }
        Brand brand = brandRepository.findById(updateSaleItemDto.getBrand().getId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found with id: " +
                        updateSaleItemDto.getBrand().getId()));
        existing.setModel(updateSaleItemDto.getModel());
        existing.setDescription(updateSaleItemDto.getDescription());
        existing.setPrice(updateSaleItemDto.getPrice());
        existing.setQuantity(updateSaleItemDto.getQuantity());
        existing.setRamGb(updateSaleItemDto.getRamGb());
        existing.setScreenSizeInch(updateSaleItemDto.getScreenSizeInch());
        existing.setStorageGb(updateSaleItemDto.getStorageGb());
        existing.setColor(updateSaleItemDto.getColor());
        SaleItem updated = repository.save(existing);
        ResponseSaleItemsDto responseDto = modelMapper.map(updated, ResponseSaleItemsDto.class);
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

    public void checkValues(SaleItem item) {
        if (item.getRamGb() == null || item.getRamGb() <= 0) {
            item.setRamGb(null);
        } if (item.getStorageGb() == null || item.getStorageGb() <= 0) {
            item.setStorageGb(null);
        } if (item.getColor() == null || item.getColor().isEmpty() || item.getColor().isBlank()) {
            item.setColor(null);
        } if (item.getScreenSizeInch() == null || item.getScreenSizeInch().doubleValue() <= 0) {
            item.setScreenSizeInch(null);
        }
    }

    public List<ListSaleItemsDto> filterSaleItemsByBrandName(List<String> filterBrands) {
        if (filterBrands == null || filterBrands.isEmpty()) {
            List<SaleItem> saleItems = repository.findAll();
            return saleItems.stream().map(item -> {
                checkValues(item);
                return modelMapper.map(item, ListSaleItemsDto.class);
            }).toList();
        } else {
            List<SaleItem> filterSaleItems = repository.findByBrandName(filterBrands);
            return filterSaleItems.stream().map(filterItem -> {
                checkValues(filterItem);
                return modelMapper.map(filterItem, ListSaleItemsDto.class);
            }).toList();
        }
    }
}
