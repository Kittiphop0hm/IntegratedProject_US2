package com.example.backend.services.saleitems;
import com.example.backend.dtos.saleItems.*;
import com.example.backend.entities.Brand;
import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemPageRepository;
import com.example.backend.repositories.SaleItemRepository;
import com.example.backend.specifications.SaleItemFilterSpecification;
import com.example.backend.specifications.SaleItemSearchSpecification;
import com.example.backend.utils.ListMapper;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
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

    public PageDto<GetSaleItemDto> mergeFilterAndSortSaleItem(
            String searchKeyword,
            List<String> filterBrands,
            Integer minPrice,
            Integer maxPrice,
            List<String> filterStorageSizesStr,
            String sortField,
            String sortDirection,
            Integer page,
            Integer size) {

        // แปลง List<String> เป็น List<Integer
        List<Integer> filterStorageSizes = new ArrayList<>();
        boolean includeNotSpecified = false;

        if (filterStorageSizesStr != null) {
            for (String s : filterStorageSizesStr) {
                if ("Not specified".equalsIgnoreCase(s) || "-1".equals(s) || "0".equals(s)) {
                    includeNotSpecified = true;
                } else {
                    try {
                        int val = Integer.parseInt(s);
                        if (val > 0) filterStorageSizes.add(val);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        // Price range
        int minPriceValue = (minPrice != null) ? minPrice : 0;
        int maxPriceValue = (maxPrice != null) ? maxPrice : ((minPrice != null) ? minPrice : Integer.MAX_VALUE);

        // สร้าง Specification สำหรับ Filter
        Specification<SaleItem> filterSpec = SaleItemFilterSpecification.buildFilterSpecification(
                filterBrands,
                (minPrice != null || maxPrice != null) ? minPriceValue : null,
                (minPrice != null || maxPrice != null) ? maxPriceValue : null,
                filterStorageSizes,
                includeNotSpecified,
                sortField,
                sortDirection
        );

        // สร้าง Specification สำหรับ Search
        Specification<SaleItem> searchSpec = SaleItemSearchSpecification.withSearchKeyword(searchKeyword);

        // รวม filter + search
        Specification<SaleItem> combinedSpec = filterSpec.and(searchSpec);

        // เรียกใช้ repository + pagination
        Page<SaleItem> saleItems = pageRepository.findAll(combinedSpec, PageRequest.of(page, size));

        // Log debug
        System.out.println("🔍 Search keyword: " + searchKeyword);
        System.out.println("🔧 Brands filter: " + filterBrands);
        System.out.println("🔧 Storage filter: " + filterStorageSizes + ", includeNotSpecified=" + includeNotSpecified);
        System.out.println("🔧 Price range: " + minPriceValue + " - " + maxPriceValue);
        System.out.println("🔧 Sort: " + sortField + " " + sortDirection);
        System.out.println("📈 Total items found: " + saleItems.getTotalElements());

        // Mapping to DTO
        return listMapper.toPageDTO(saleItems, GetSaleItemDto.class, modelMapper, sortField);
    }
}
