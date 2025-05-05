package com.example.backend.services;

import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.SaleItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import static java.lang.System.in;

@Service
public class SaleItemService {
    @Autowired
    private SaleItemRepository repository;

    public List<SaleItem> findAll() {
        return repository.findAll();
    }

    public SaleItem findById(int id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("SaleItem not found for this id :: " + id));
    }

    public SaleItem checkValue(SaleItem saleItem) {
        if (saleItem.getColor().contains("null")) {
            saleItem.setColor(null);
        } if (saleItem.getRamGb() == 0) {
            saleItem.setRamGb(null);
        } if (saleItem.getStorageGb() == 0) {
            saleItem.setStorageGb(null);
        } if (saleItem.getScreenSizeInch().doubleValue() <= 0) {
            saleItem.setScreenSizeInch(null);
        }
        return saleItem;
    }

    public List<SaleItem> checkValues(List<SaleItem> saleItems) {
        for (SaleItem item : saleItems) {
            checkValue(item);
        }
        return saleItems;
    }
}
