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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        List<Picture> images = pictureRepository.findBySalesIdOrderByImageViewOrderAsc(id);
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

    @Transactional
    public ResponseSaleItemsDto updateProduct(Integer id, SaleItemWithImageInfo data) {
        saleItemServiceV1.updateSaleItem(id,data.getSaleItem());
        List<SaleItemImageRequest>imageInfos = data.getImageInfos();
        imageInfos.forEach(
                imageInfo -> {
                    switch (imageInfo.getStatus()) {
                        case "ONLINE":
                            break;
                        case "DELETE":
                            fileService.removeFile(imageInfo.getFileName());
                            break;
                        case "MOVE":
                            String originalName = StringUtils.cleanPath(imageInfo.getImageFile().getOriginalFilename());
                            String extension = originalName.substring(originalName.lastIndexOf("."));
                            String newName = imageInfo.getOrder() + extension;
                            Picture pic = pictureRepository.findPictureByFileName(imageInfo.getFileName());
                            fileService.renameFile(imageInfo.getFileName(), newName);
                            pic.setImageViewOrder(imageInfo.getOrder());
                            pic.setFileName(newName);
                            pictureRepository.save(pic);
                            break;
                        case "NEW":
                            fileService.store(imageInfo.getImageFile(), id , imageInfo.getOrder());
                            break;
                    }
                }

        );
            imageInfos.forEach(
                    imageInfo -> {
                        Picture picCheck  = pictureRepository.findBySalesIdAndImageViewOrder(id, imageInfo.getOrder());
                        if(picCheck != null && !picCheck.getFileName().contains(id.toString())) {
                            String originalName = StringUtils.cleanPath(imageInfo.getImageFile().getOriginalFilename());
                            String extension = originalName.substring(originalName.lastIndexOf("."));
                            String oldName = imageInfo.getOrder()+"" + extension;
                            String newName = id + "." + imageInfo.getOrder() + extension;
                            Picture pic = pictureRepository.findPictureByFileName(oldName);
                            fileService.renameFile(oldName, newName);
                            if(pic != null) {
                                pic.setFileName(newName);
                                pictureRepository.save(pic);

                            }
                        }
                    }
            );
//        List<Picture> pics = pictureRepository.findBySalesIdOrderByImageViewOrderAsc(id);
//        List<String> fileNames = pics.stream().map(Picture::getFileName).toList();
//        for(String fileName : fileNames) {
//            fileService.removeFile(fileName);
//        }
//        List<SaleItemImageRequest> imagesReq = data.getImageInfos();
//        List<MultipartFile> fileList = imagesReq
//                .stream()
//                .map(SaleItemImageRequest::getImageFile).toList();
//        fileService.storeList(fileList,id);
        return findByid(id);
        }
    }

