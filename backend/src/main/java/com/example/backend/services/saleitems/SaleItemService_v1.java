package com.example.backend.services.saleitems;
import com.example.backend.dtos.saleItems.*;
import com.example.backend.dtos.saleItems.sellers.GetSaleItemSellerDto;
import com.example.backend.dtos.saleItems.sellers.ResponseSaleItemsWithSellerDto;
import com.example.backend.entities.AuthUserDetail;
import com.example.backend.entities.Brand;
import com.example.backend.entities.SaleItem;
import com.example.backend.entities.User;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.SaleItemPageRepository;
import com.example.backend.repositories.SaleItemRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.users.JwtUserDetailsService;
import com.example.backend.specifications.SaleItemSpecification;
import com.example.backend.utils.ListMapper;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

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

    @Transactional
    public ResponseSaleItemsWithSellerDto createSaleItem(SaleItemDetailForCreateOrUpdateDto createSaleItemDto , Integer id) {
        brandRepository.findById(createSaleItemDto.getBrand().getId())
                .orElseThrow(() -> new ItemNotFoundException("Brand not found for this id :: " + createSaleItemDto.getBrand().getId()));
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found for this id :: " + id));
        SaleItem saleItem = modelMapper.map(createSaleItemDto, SaleItem.class);
        saleItem.setSeller(user);
        SaleItem savedSaleItem = repository.save(saleItem);
        entityManager.refresh(savedSaleItem);
        ResponseSaleItemsWithSellerDto responseDto = modelMapper.map(savedSaleItem, ResponseSaleItemsWithSellerDto.class);
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

    public PageDto<GetSaleItemDto> FilterAndSortSaleItem(
            String searchKeyword,
            List<String> filterBrands,
            Integer minPrice,
            Integer maxPrice,
            List<String> filterStorageSizesStr,
            String sortField,
            String sortDirection,
            Integer page,
            Integer size ,
            AuthUserDetail principal
            ) {
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
                    } catch (NumberFormatException ignored) {

                    }
                }
            }
        }
        Specification<SaleItem> combinedSpec = SaleItemSpecification.buildFilterSpecification(
                searchKeyword,
                filterBrands,
                minPrice,
                maxPrice,
                filterStorageSizes,
                includeNotSpecified,
                sortField,
                sortDirection
        );

        Page<SaleItem> saleItems = pageRepository.findAll(combinedSpec, PageRequest.of(page, size));

        // Log debug
        System.out.println("🔍 Search keyword: " + searchKeyword);
        System.out.println("🔧 Brands filter: " + filterBrands);
        System.out.println("🔧 Storage filter: " + filterStorageSizes + ", includeNotSpecified=" + includeNotSpecified);
        System.out.println("💰 Price range: " + minPrice + " - " + maxPrice);
        System.out.println("🔧 Sort: " + sortField + " " + sortDirection);
        System.out.println("📄 Page: " + page + ", Size: " + size);
        System.out.println("📈 Total items found: " + saleItems.getTotalElements());
        System.out.println("📊 Total pages: " + saleItems.getTotalPages());
        System.out.println(principal);
        if(principal != null) {
//            PageDto<GetSaleItemDto> saleItemPages = modelMapper.map(saleItems, PageDto.class);
            Page<GetSaleItemDto> dtoPage = saleItems.map(items -> {
                GetSaleItemDto dto = modelMapper.map(items, GetSaleItemDto.class);
                dto.setIsOwnedByCurrentUser(items.getSeller().getId().equals(principal.getId()));
                return dto;
            });
            PageDto<GetSaleItemDto> pageDtos = modelMapper.map(dtoPage, PageDto.class);
            pageDtos.setContent(pageDtos.getContent());
            pageDtos.setSort(sortField);
            return pageDtos;
        }
        return listMapper.toPageDTO(saleItems, GetSaleItemDto.class, modelMapper, sortField);
    }


    public PageDto<GetSaleItemSellerDto> getSaleItemListBySeller(Integer id , Integer page , Integer size, String sortField, String sortDirection , AuthUserDetail principal) {
//        System.out.println(("principal.getId()"));
//        System.out.println(principal.getId());
        System.out.println("getSaleItemListBySeller function");
        System.out.println("seller"+principal);
        if (!principal.getId().equals(id)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
//                    "Seller id not matched with id in access token");
            throw new AccessDeniedException("Not allowed to access other seller's resources");
        }
        //        UserDetails user1 = jwtUserDetailsService.loadUserById(id);
//        AuthUserDetail user2 = jwtUserDetailsService.loadUserByIds(id);
//        System.out.println("user1");
//        System.out.println(user1);
//        System.out.println("user2.getId()");
//        System.out.println(user2.getId());
        Page<SaleItem> saleItems = pageRepository.findBySellerIdOrderByCreatedOnAsc(id, PageRequest.of(page, size));
        return listMapper.toPageDTO(saleItems, GetSaleItemSellerDto.class, modelMapper, sortField);
    }
//    // PBI 25 smark
//    public PageDto<GetSaleItemDto> getSaleItemsBySellerId(
//            Integer sellerId, int page, int size, String sortField, String sortDirection) {
//
//        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
//        PageRequest pageRequest = PageRequest.of(page, size, sort);
//
//        Page<SaleItem> saleItemPage = repository.findBySellerId(sellerId, pageRequest);
//
//        List<GetSaleItemDto> saleItemDtos = saleItemPage.getContent().stream()
//                .map(saleItem -> modelMapper.map(saleItem, GetSaleItemDto.class))
//                .collect(Collectors.toList());
//
//        PageDto<GetSaleItemDto> pageDto = new PageDto<>();
//        pageDto.setContent(saleItemDtos);
//        pageDto.setTotalPages(saleItemPage.getTotalPages());
//        pageDto.setTotalElements((int) saleItemPage.getTotalElements());
//        pageDto.setSize(saleItemPage.getSize());
//        pageDto.setNumber(saleItemPage.getNumber());
//        pageDto.setFirst(saleItemPage.isFirst());
//        pageDto.setLast(saleItemPage.isLast());
//        pageDto.setSort(saleItemPage.getSort().toString());
//
//        return pageDto;
//    }

}
