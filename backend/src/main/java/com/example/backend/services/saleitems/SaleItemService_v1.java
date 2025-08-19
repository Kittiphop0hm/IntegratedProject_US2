package com.example.backend.services.saleitems;
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
            List<String> filterBrands,
            Integer minPrice,
            Integer maxPrice,
            List<String> filterStorageSizesStr,
            String sortField,
            String sortDirection,
            Integer page,
            Integer size) {

        // แปลง List<String> เป็น List<Integer>
        List<Integer> filterStorageSizes = new ArrayList<>();
        boolean includeNotSpecified = false;

        if (filterStorageSizesStr != null && !filterStorageSizesStr.isEmpty()) {
            System.out.println("🔍 Received filterStorageSizesStr: " + filterStorageSizesStr);

            for (String s : filterStorageSizesStr) {
                if (s.equalsIgnoreCase("Not specified") || s.equals("-1") || s.equals("0")) {
                    includeNotSpecified = true;
                    System.out.println("✅ Found 0, -1 or 'Not specified': " + s);
                } else {
                    try {
                        Integer value = Integer.parseInt(s);
                        if (value > 0) {
                            filterStorageSizes.add(value);
                        }
                    } catch (NumberFormatException e) {
                        // ignore invalid values
                    }
                }
            }
        }

        System.out.println("📊 Final includeNotSpecified: " + includeNotSpecified);
        System.out.println("📊 Final filterStorageSizes: " + filterStorageSizes);

        // ใช้ logic เดียวกับโค้ดเดิม
        boolean hasBrandFilter = filterBrands != null && !filterBrands.isEmpty();
        boolean hasStorageFilter = filterStorageSizes != null && !filterStorageSizes.isEmpty();
        boolean hasPriceFilter = minPrice != null || maxPrice != null;

        int minPriceValue = (minPrice != null) ? minPrice : 0;
        int maxPriceValue;
        if (maxPrice != null) {
            maxPriceValue = maxPrice;
        } else if (minPrice != null) {
            maxPriceValue = minPrice;
        } else {
            maxPriceValue = Integer.MAX_VALUE;
        }

        // ปรับปรุง sorting logic - รองรับ sortField = "brand.name" หรือ "brand"
        boolean isBrandSort = "brand.name".equals(sortField) || "brand".equals(sortField);
        boolean isCreatedOnSort = "createdOn".equals(sortField) || "created_on".equals(sortField);

        // กำหนด default sorting behavior
        boolean isDefaultSort = (sortField == null || sortField.isEmpty());

        // กำหนด sort direction
        boolean isAscending;
        if (isDefaultSort) {
            // Default จะเรียงตาม createdOn ASC
            isAscending = true;
        } else {
            // ถ้ามี sortField แต่ไม่มี sortDirection ให้เป็น ASC
            isAscending = sortDirection == null || sortDirection.isEmpty() || "asc".equalsIgnoreCase(sortDirection);
        }

        System.out.println("🔧 Sort Debug - sortField: " + sortField + ", sortDirection: " + sortDirection);
        System.out.println("🔧 Sort Debug - isBrandSort: " + isBrandSort + ", isCreatedOnSort: " + isCreatedOnSort + ", isDefaultSort: " + isDefaultSort + ", isAscending: " + isAscending);

        Page<SaleItem> saleItems;

        if (!hasPriceFilter && !hasStorageFilter && !includeNotSpecified) {
            // กรองเฉพาะ brand + sort (ไม่มี price, storage, not specified)
            if (!hasBrandFilter) {
                if (isDefaultSort || isCreatedOnSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findAllByOrderByCreatedOnAsc(PageRequest.of(page, size));
                        System.out.println("✅ no filter createdOn ASC");
                    } else {
                        saleItems = pageRepository.findAllByOrderByCreatedOnDesc(PageRequest.of(page, size));
                        System.out.println("✅ no filter createdOn DESC");
                    }
                } else if (isBrandSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findAllByOrderByBrandNameAsc(PageRequest.of(page, size));
                        System.out.println("✅ no filter brand ASC");
                    } else {
                        saleItems = pageRepository.findAllByOrderByBrandNameDesc(PageRequest.of(page, size));
                        System.out.println("✅ no filter brand DESC");
                    }
                } else {
                    // fallback to default
                    saleItems = pageRepository.findAllByOrderByCreatedOnAsc(PageRequest.of(page, size));
                    System.out.println("✅ no filter fallback createdOn ASC");
                }
            } else {
                if (isDefaultSort || isCreatedOnSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findByBrand_NameInOrderByCreatedOnAsc(filterBrands, PageRequest.of(page, size));
                        System.out.println("✅ filter createdOn ASC");
                    } else {
                        saleItems = pageRepository.findByBrand_NameInOrderByCreatedOnDesc(filterBrands, PageRequest.of(page, size));
                        System.out.println("✅ filter createdOn DESC");
                    }
                } else if (isBrandSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findByBrand_NameInOrderByBrand_NameAsc(filterBrands, PageRequest.of(page, size));
                        System.out.println("✅ filter brand ASC");
                    } else {
                        saleItems = pageRepository.findByBrand_NameInOrderByBrand_NameDesc(filterBrands, PageRequest.of(page, size));
                        System.out.println("✅ filter brand DESC");
                    }
                } else {
                    // fallback to default
                    saleItems = pageRepository.findByBrand_NameInOrderByCreatedOnAsc(filterBrands, PageRequest.of(page, size));
                    System.out.println("✅ filter fallback createdOn ASC");
                }
            }
        } else if (hasPriceFilter && !hasStorageFilter && !includeNotSpecified) {
            // กรอง price + brand แต่ไม่มี storage และ no not specified
            if (!hasBrandFilter) {
                if (isDefaultSort || isCreatedOnSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findByPriceBetweenOrderByCreatedOnAsc(minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    } else {
                        saleItems = pageRepository.findByPriceBetweenOrderByCreatedOnDesc(minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    }
                } else if (isBrandSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findByPriceBetweenOrderByBrand_NameAsc(minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    } else {
                        saleItems = pageRepository.findByPriceBetweenOrderByBrand_NameDesc(minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    }
                } else {
                    saleItems = pageRepository.findByPriceBetweenOrderByCreatedOnAsc(minPriceValue, maxPriceValue, PageRequest.of(page, size));
                }
            } else {
                if (isDefaultSort || isCreatedOnSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findByBrand_NameInAndPriceBetweenOrderByCreatedOnAsc(filterBrands, minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    } else {
                        saleItems = pageRepository.findByBrand_NameInAndPriceBetweenOrderByCreatedOnDesc(filterBrands, minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    }
                } else if (isBrandSort) {
                    if (isAscending) {
                        saleItems = pageRepository.findByBrand_NameInAndPriceBetweenOrderByBrand_NameAsc(
                                filterBrands, minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    } else {
                        saleItems = pageRepository.findByBrand_NameInAndPriceBetweenOrderByBrand_NameDesc(
                                filterBrands, minPriceValue, maxPriceValue, PageRequest.of(page, size));
                    }
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenOrderByCreatedOnAsc(filterBrands, minPriceValue, maxPriceValue, PageRequest.of(page, size));
                }
            }
        } else if (!hasBrandFilter && !hasPriceFilter && hasStorageFilter && !includeNotSpecified) {
            // กรอง storage อย่างเดียว (ไม่มี brand, ไม่มี price, no not specified)
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByStorageGbInOrderByCreatedOnAsc(filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByStorageGbInOrderByCreatedOnDesc(filterStorageSizes, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByStorageGbInOrderByBrand_NameAsc(filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByStorageGbInOrderByBrand_NameDesc(filterStorageSizes, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByStorageGbInOrderByCreatedOnAsc(filterStorageSizes, PageRequest.of(page, size));
            }
        } else if (!hasBrandFilter && hasPriceFilter && hasStorageFilter && !includeNotSpecified) {
            // กรอง price + storage แต่ไม่มี brand (no not specified)
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrderByCreatedOnAsc(
                            minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrderByCreatedOnDesc(
                            minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrderByBrand_NameAsc(
                            minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrderByBrand_NameDesc(
                            minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrderByCreatedOnAsc(
                        minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
            }
        } else if (!hasBrandFilter && !hasPriceFilter && !hasStorageFilter && includeNotSpecified) {
            // กรองกรณี Not specified storageGb เท่านั้น (no brand, no price, no storage)
            System.out.println("🎯 Calling findByStorageGbEquals(0)");
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByStorageGbEqualsOrderByCreatedOnAsc(0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByStorageGbEqualsOrderByCreatedOnDesc(0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByStorageGbEqualsOrderByBrand_NameAsc(0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByStorageGbEqualsOrderByBrand_NameDesc(0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByStorageGbEqualsOrderByCreatedOnAsc(0, PageRequest.of(page, size));
            }
            System.out.println("📈 Found " + saleItems.getTotalElements() + " items with storageGb = 0");
        } else if (!hasBrandFilter && hasPriceFilter && !hasStorageFilter && includeNotSpecified) {
            // กรอง price + Not specified storageGb (no brand)
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbEqualsOrderByCreatedOnAsc(
                            minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbEqualsOrderByCreatedOnDesc(
                            minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbEqualsOrderByBrand_NameAsc(
                            minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbEqualsOrderByBrand_NameDesc(
                            minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbEqualsOrderByCreatedOnAsc(
                        minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
            }
        } else if (!hasBrandFilter && !hasPriceFilter && hasStorageFilter && includeNotSpecified) {
            // กรอง storage (normal list) + Not specified storageGb (no brand, no price)
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(filterStorageSizes, 0, PageRequest.of(page, size));
            }
        } else if (!hasBrandFilter && hasPriceFilter && hasStorageFilter && includeNotSpecified) {
            // กรอง price + storage (list) + Not specified storageGb (no brand)
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
                            minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(
                            minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(
                            minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(
                            minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
                        minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
            }
        } else if (hasBrandFilter && !hasPriceFilter && !hasStorageFilter && includeNotSpecified) {
            // กรอง brand + Not specified storageGb เท่านั้น
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbEqualsOrderByCreatedOnAsc(
                            filterBrands, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbEqualsOrderByCreatedOnDesc(
                            filterBrands, 0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbEqualsOrderByBrand_NameAsc(
                            filterBrands, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbEqualsOrderByBrand_NameDesc(
                            filterBrands, 0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByBrand_NameInAndStorageGbEqualsOrderByCreatedOnAsc(
                        filterBrands, 0, PageRequest.of(page, size));
            }
        } else if (hasBrandFilter && hasPriceFilter && !hasStorageFilter && includeNotSpecified) {
            // กรอง brand + price + Not specified storageGb
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByCreatedOnAsc(
                            filterBrands, minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByCreatedOnDesc(
                            filterBrands, minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByBrand_NameAsc(
                            filterBrands, minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByBrand_NameDesc(
                            filterBrands, minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbEqualsOrderByCreatedOnAsc(
                        filterBrands, minPriceValue, maxPriceValue, 0, PageRequest.of(page, size));
            }
        } else if (hasBrandFilter && !hasPriceFilter && hasStorageFilter && includeNotSpecified) {
            // กรอง brand + storage (list) + Not specified storageGb
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
                            filterBrands, filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(
                            filterBrands, filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(
                            filterBrands, filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(
                            filterBrands, filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
                        filterBrands, filterStorageSizes, 0, PageRequest.of(page, size));
            }
        } else if (hasBrandFilter && !hasPriceFilter && hasStorageFilter && !includeNotSpecified) {
            // กรอง brand + storage (list) แต่ไม่มี Not specified
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrderByCreatedOnAsc(
                            filterBrands, filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrderByCreatedOnDesc(
                            filterBrands, filterStorageSizes, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrderByBrand_NameAsc(
                            filterBrands, filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrderByBrand_NameDesc(
                            filterBrands, filterStorageSizes, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByBrand_NameInAndStorageGbInOrderByCreatedOnAsc(
                        filterBrands, filterStorageSizes, PageRequest.of(page, size));
            }
        } else if (hasBrandFilter && hasPriceFilter && hasStorageFilter && !includeNotSpecified) {
            // ✅ กรอง brand + price + storage (list) แต่ไม่มี Not specified - เพิ่มกรณีนี้ใหม่!
            System.out.println("🔥 Brand + Price + Storage (NO Not specified)");
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByCreatedOnAsc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByCreatedOnDesc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByBrand_NameAsc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByBrand_NameDesc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrderByCreatedOnAsc(
                        filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, PageRequest.of(page, size));
            }
        } else {
            // กรอง brand + price + storage (list) + Not specified storageGb (กรณีมีครบทุกอย่าง)
            System.out.println("🔥 Brand + Price + Storage + Not specified");
            if (isDefaultSort || isCreatedOnSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnDesc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else if (isBrandSort) {
                if (isAscending) {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameAsc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                } else {
                    saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByBrand_NameDesc(
                            filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
                }
            } else {
                saleItems = pageRepository.findByBrand_NameInAndPriceBetweenAndStorageGbInOrStorageGbEqualsOrderByCreatedOnAsc(
                        filterBrands, minPriceValue, maxPriceValue, filterStorageSizes, 0, PageRequest.of(page, size));
            }
        }

        return listMapper.toPageDTO(saleItems, GetSaleItemDto.class, modelMapper, sortField);
    }
}
