package com.example.backend.dtos.saleItems;

import com.example.backend.dtos.brands.ListBrandsDto;
import com.example.backend.dtos.files.SaleItemImageRequest;
import lombok.Data;

import java.util.List;

@Data
public class SaleItemWithImageInfo {
    private SaleItemDetailForCreateOrUpdateDto saleItem;
    private List<SaleItemImageRequest> imageInfos;
}
