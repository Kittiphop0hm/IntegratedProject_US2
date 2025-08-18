package com.example.backend.services.saleitems;

import com.example.backend.dtos.files.ListFilesDto;
import com.example.backend.dtos.files.SaleItemImageRequest;
import com.example.backend.dtos.saleItems.ListSaleItemsDto;
import com.example.backend.dtos.saleItems.ResponseSaleItemsDto;
import com.example.backend.dtos.saleItems.SaleItemDetailForCreateOrUpdateDto;
import com.example.backend.dtos.saleItems.SaleItemWithImageInfo;
import com.example.backend.entities.Picture;
import com.example.backend.entities.SaleItem;
import com.example.backend.exceptions.ItemNotFoundException;
import com.example.backend.repositories.BrandRepository;
import com.example.backend.repositories.PictureRepository;
import com.example.backend.repositories.SaleItemRepository;
import com.example.backend.services.FileService;
import com.example.backend.utils.ListMapper;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleItemService_v2 {

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SaleItemService_v1 saleItemServiceV1;

    @Autowired
    private FileService fileService;
    @Autowired
    private ListMapper listMapper;

    public ResponseSaleItemsDto findByid(Integer id) {
        SaleItem saleItem = saleItemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("SaleItem not found for this id :: " + id));
        ResponseSaleItemsDto mainDto = modelMapper.map(saleItem,ResponseSaleItemsDto.class);
        List<Picture> images = pictureRepository.findBySales_Id(id);
        List<ListFilesDto> listFile = listMapper.mapList(images,ListFilesDto.class,modelMapper);
        mainDto.setSaleItemImages(listFile);
        return mainDto;
    }

    public ResponseSaleItemsDto createProduct(SaleItemDetailForCreateOrUpdateDto createSaleItemDto , List<MultipartFile> images) {
        ResponseSaleItemsDto mainDto = saleItemServiceV1.createSaleItem(createSaleItemDto);
        fileService.storeList(images , mainDto.getId());
        return findByid(mainDto.getId());
    }

    public void deleteProduct(Integer id) {
        if(!saleItemRepository.existsById(id)) {
            throw new ItemNotFoundException("Sale item does not exist");
        }

        List<Picture> pics = pictureRepository.findBySales_Id(id);
        List<String> fileNames = pics.stream().map(Picture::getFileName).toList();
        for(String fileName : fileNames) {
            fileService.removeFile(fileName);
        }
        saleItemRepository.deleteById(id);
    }

    public ResponseSaleItemsDto updateProduct(Integer id, SaleItemWithImageInfo data) {
        saleItemServiceV1.updateSaleItem(id,data.getSaleItem());
        List<Picture> pics = pictureRepository.findBySales_Id(id);
        List<String> fileNames = pics.stream().map(Picture::getFileName).toList();
        for(String fileName : fileNames) {
            fileService.removeFile(fileName);
        }
        List<SaleItemImageRequest> imagesReq = data.getImageInfos();
        List<MultipartFile> fileList = imagesReq
                .stream()
                .map(SaleItemImageRequest::getImageFile).toList();
        fileService.storeList(fileList,id);
        return findByid(id);
        }
    }

