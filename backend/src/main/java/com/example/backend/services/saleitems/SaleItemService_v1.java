package com.example.backend.services;
import com.example.backend.dtos.saleItems.*;
import com.example.backend.entities.Brand;

import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;

import com.example.backend.repositories.SaleItemPageRepository;
import com.example.backend.repositories.SaleItemRepository;
import com.example.backend.utils.ListMapper;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;





@Service
public class SaleItemService_v1 {
    @Autowired
    private SaleItemRepository repository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private SaleItemPageRepository pageRepository;

    public List<ListSaleItemsDto> findAll() {
        List<SaleItem> saleItems = repository.findAllByOrderByCreatedOn();
        return saleItems.stream().map(item -> modelMapper.map(item, ListSaleItemsDto.class)).toList();
    }

    public GetSaleItemDto findById(int id) {
        SaleItem saleItem = getSaleItemById(id);
        return modelMapper.map(saleItem, GetSaleItemDto.class);
    }

    @Transactional
    public ResponseSaleItemsDto createSaleItem(SaleItemDetailForCreateOrUpdateDto createSaleItemDto) {
        brandRepository.findById(createSaleItemDto.getBrand().getId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + createSaleItemDto.getBrand().getId()));
        SaleItem saleItem = modelMapper.map(createSaleItemDto, SaleItem.class);
        SaleItem savedSaleItem = repository.save(saleItem);
        entityManager.refresh(savedSaleItem);
        ResponseSaleItemsDto responseDto = modelMapper.map(savedSaleItem, ResponseSaleItemsDto.class);
        responseDto.setBrandName(savedSaleItem.getBrand().getName());
        return responseDto;
    }

    public ResponseSaleItemsDto updateSaleItem(int id, SaleItemDetailForCreateOrUpdateDto updateSaleItemDto) {
        SaleItem existing = getSaleItemById(id);
        if (updateSaleItemDto.getBrand() == null || updateSaleItemDto.getBrand().getId() == null) {
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
        }
        if (item.getStorageGb() == null || item.getStorageGb() <= 0) {
            item.setStorageGb(null);
        }
        if (item.getColor() == null || item.getColor().isEmpty() || item.getColor().isBlank()) {
            item.setColor(null);
        }
        if (item.getScreenSizeInch() == null || item.getScreenSizeInch().doubleValue() <= 0) {
            item.setScreenSizeInch(null);
        }
    }

    public PageDto<GetSaleItemDto> mergeFilterAndSortSaleItem(List<String> filterBrands, String sortField, String sortDirection , Integer page , Integer size) {
        Page<SaleItem> saleItems;
        if (filterBrands.isEmpty()) {
            System.out.println(sortDirection);
            if (sortField.isEmpty() && sortDirection.isEmpty()) {
                saleItems = pageRepository.findAllByOrderByCreatedOn(PageRequest.of(page,size));
                System.out.println("no filter createOn");
            }   else if (sortDirection.equalsIgnoreCase("asc") || sortDirection.isEmpty()) {
                System.out.println("no filter Asc");
                saleItems = pageRepository.findAllByOrderByBrandNameAsc(PageRequest.of(page,size));
            }   else {
                saleItems = pageRepository.findAllByOrderByBrandNameDesc(PageRequest.of(page,size));
            }
        } else {
            System.out.println(sortDirection);
            if (sortField.isEmpty() && sortDirection.isEmpty() ) {
                System.out.println("filter createOn");
                saleItems = pageRepository.findByBrand_NameInOrderByBrand_CreatedOn(filterBrands, PageRequest.of(page,size));
            } else if (sortDirection.equalsIgnoreCase("asc") || sortDirection.isEmpty()) {
                System.out.println("filter Asc");
                saleItems = pageRepository.findByBrand_NameInOrderByBrand_NameAsc(filterBrands , PageRequest.of(page,size));
            } else {
                saleItems = pageRepository.findByBrand_NameInOrderByBrand_NameDesc(filterBrands , PageRequest.of(page,size));
            }
        }
        return listMapper.toPageDTO(saleItems , GetSaleItemDto.class , modelMapper , sortField);
    }
}
