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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PageDto<GetSaleItemDto> mergeFilterAndSortSaleItem(
            List<String> filterBrands,
            Integer minPrice,
            Integer maxPrice,
            List<String> filterStorageSizesStr,
            String sortField,
            String sortDirection,
            Integer page,
            Integer size) {

        // แปลง List<String> เป็น List<Integer>
        List<Integer> filterStorageSizes = List.of();
        boolean includeNotSpecified = false;
        if (filterStorageSizesStr != null && !filterStorageSizesStr.isEmpty()) {
            filterStorageSizes = filterStorageSizesStr.stream()
                    .filter(s -> !s.equalsIgnoreCase("Not specified"))
                    .map(s -> {
                        try {
                            return Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    })
                    .filter(i -> i != null)
                    .toList();

            includeNotSpecified = filterStorageSizesStr.stream()
                    .anyMatch(s -> s.equalsIgnoreCase("Not specified"));
        }


        String sortBy = (sortField == null || sortField.isEmpty()) ? "createdOn" : sortField;
        boolean hasBrandFilter = filterBrands != null && !filterBrands.isEmpty();
        boolean hasStorageFilter = filterStorageSizes != null && !filterStorageSizes.isEmpty();
        boolean hasPriceFilter = minPrice != null || maxPrice != null;

        int minPriceValue = (minPrice != null) ? minPrice : 0;
        int maxPriceValue = (maxPrice != null) ? maxPrice : Integer.MAX_VALUE;

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<SaleItem> saleItems;

        if (!hasPriceFilter && !hasStorageFilter && !includeNotSpecified) {
            // กรองเฉพาะ brand + sort (ไม่มี price, storage, not specified)
            if (!hasBrandFilter) {
                if ((sortField == null || sortField.isEmpty()) && (sortDirection == null || sortDirection.isEmpty())) {
                    saleItems = pageRepository.findAllByOrderByCreatedOn(pageable);
                } else if ("asc".equalsIgnoreCase(sortDirection) || (sortDirection == null || sortDirection.isEmpty())) {
                    saleItems = pageRepository.findAllByOrderByBrandNameAsc(pageable);
                } else {
                    saleItems = pageRepository.findAllByOrderByBrandNameDesc(pageable);
                }
            } else {
                if ((sortField == null || sortField.isEmpty()) && (sortDirection == null || sortDirection.isEmpty())) {
                    saleItems = pageRepository.findByBrand_NameInOrderByBrand_CreatedOn(filterBrands, pageable);
                } else if ("asc".equalsIgnoreCase(sortDirection) || (sortDirection == null || sortDirection.isEmpty())) {
                    saleItems = pageRepository.findByBrand_NameInOrderByBrand_NameAsc(filterBrands, pageable);
                } else {
                    saleItems = pageRepository.findByBrand_NameInOrderByBrand_NameDesc(filterBrands, pageable);
                }
            }
        } else if (hasPriceFilter && !hasStorageFilter && !includeNotSpecified) {
            // กรอง price + brand แต่ไม่มี storage และ no not specified
            if (!hasBrandFilter) {
                if (direction == Sort.Direction.ASC) {
                    saleItems = pageRepository.findByPriceBetweenOrderByBrand_NameAsc(minPriceValue, maxPriceValue, pageable);
                } else {
                    saleItems = pageRepository.findByPriceBetweenOrderByBrand_NameDesc(minPriceValue, maxPriceValue, pageable);
                }
            } else {
                // กรอง price + brand
                if (direction == Sort.Direction.ASC) {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenOrderByBrand_NameAsc(
                            filterBrands, minPriceValue, maxPriceValue, pageable);
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenOrderByBrand_NameDesc(
                            filterBrands, minPriceValue, maxPriceValue, pageable);
                }
            }
        } else if (!hasBrandFilter && !hasPriceFilter && hasStorageFilter && !includeNotSpecified) {
            // กรอง storage อย่างเดียว (ไม่มี brand, ไม่มี price, no not specified)
            if (direction == Sort.Direction.ASC) {
                saleItems = pageRepository.findByStorageGbInOrderByBrand_NameAsc(filterStorageSizes, pageable);
            } else {
                saleItems = pageRepository.findByStorageGbInOrderByBrand_NameDesc(filterStorageSizes, pageable);
            }
        } else if (!hasBrandFilter && hasPriceFilter && hasStorageFilter && !includeNotSpecified) {
            // กรอง price + storage แต่ไม่มี brand (no not specified)
            if (direction == Sort.Direction.ASC) {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrderByBrand_NameAsc(
                        minPriceValue, maxPriceValue, filterStorageSizes, pageable);
            } else {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrderByBrand_NameDesc(
                        minPriceValue, maxPriceValue, filterStorageSizes, pageable);
            }
        } else if (!hasBrandFilter && !hasPriceFilter && !hasStorageFilter && includeNotSpecified) {
            // กรองกรณี Not specified storageGb เท่านั้น (no brand, no price, no storage)
            if (direction == Sort.Direction.ASC) {
                saleItems = pageRepository.findByStorageGbIsNullOrderByBrand_NameAsc(pageable);
            } else {
                saleItems = pageRepository.findByStorageGbIsNullOrderByBrand_NameDesc(pageable);
            }
        } else if (!hasBrandFilter && hasPriceFilter && !hasStorageFilter && includeNotSpecified) {
            // กรอง price + Not specified storageGb (no brand)
            if (direction == Sort.Direction.ASC) {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbIsNullOrderByBrand_NameAsc(
                        minPriceValue, maxPriceValue, pageable);
            } else {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbIsNullOrderByBrand_NameDesc(
                        minPriceValue, maxPriceValue, pageable);
            }
        } else if (!hasBrandFilter && !hasPriceFilter && hasStorageFilter && includeNotSpecified) {
            // กรอง storage (normal list) + Not specified storageGb (no brand, no price)
            if (direction == Sort.Direction.ASC) {
                saleItems = pageRepository.findByStorageGbInOrStorageGbIsNullOrderByBrand_NameAsc(filterStorageSizes, pageable);
            } else {
                saleItems = pageRepository.findByStorageGbInOrStorageGbIsNullOrderByBrand_NameDesc(filterStorageSizes, pageable);
            }
        } else if (!hasBrandFilter && hasPriceFilter && hasStorageFilter && includeNotSpecified) {
            // กรอง price + storage (list) + Not specified storageGb (no brand)
            if (direction == Sort.Direction.ASC) {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameAsc(
                        minPriceValue, maxPriceValue, filterStorageSizes, pageable);
            } else {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameDesc(
                        minPriceValue, maxPriceValue, filterStorageSizes, pageable);
            }
        } else {
            // กรอง price + brand + storage (list) + Not specified storageGb
            if (direction == Sort.Direction.ASC) {
                saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameAsc(
                        hasBrandFilter ? filterBrands : List.of(),
                        minPriceValue,
                        maxPriceValue,
                        filterStorageSizes,
                        pageable);
            } else {
                saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbIsNullOrderByBrand_NameDesc(
                        hasBrandFilter ? filterBrands : List.of(),
                        minPriceValue,
                        maxPriceValue,
                        filterStorageSizes,
                        pageable);
            }
        }

        return listMapper.toPageDTO(saleItems, GetSaleItemDto.class, modelMapper, sortBy);
    }
}
