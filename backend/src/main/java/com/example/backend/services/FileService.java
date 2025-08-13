package com.example.backend.services;

import com.example.backend.entities.Picture;
import com.example.backend.entities.SaleItem;
import com.example.backend.repositories.PictureRepository;
import com.example.backend.repositories.SaleItemRepository;
import com.example.backend.utils.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    private SaleItemRepository saleItemRepository;
    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties){
        this.fileStorageProperties = fileStorageProperties;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try{
            if(!Files.exists(this.fileStorageLocation)){
                Files.createDirectories(this.fileStorageLocation);
            }
        } catch(IOException e){
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String store(MultipartFile file , Integer saleId , Integer order ){

        SaleItem saleItem = saleItemRepository.findById(saleId).orElseThrow(() -> new RuntimeException("SaleItem not found"));

        if(!isSupportedContentType(file)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The content type of the file is not supported." + file.getContentType());
        }
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());

        String extension = originalName.substring(originalName.lastIndexOf("."));
        String realName = UUID.randomUUID() + extension;
        try {
            Path targetLocation = this.fileStorageLocation.resolve(realName);
            Picture picture = new Picture();
            picture.setFileName(saleId + "." + order);
            picture.setImageViewOrder(order);
            picture.setSales(saleItem);
            Files.copy(file.getInputStream() , targetLocation , StandardCopyOption.REPLACE_EXISTING);
            pictureRepository.save(picture);
            return originalName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + originalName, e);
        }
    }

    public Resource loadFileAsResource(String fileName){
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load file " + fileName, e);
        }
    }

    public List<String> getImageList(){
        File folder = fileStorageLocation.toFile();
        List<String> extensions = Arrays.stream(fileStorageProperties.getSupportFileTypes())
                .map(type -> "." + type.substring(type.indexOf("/")+ 1))
                .toList();
        File[] listOfFiles = folder.listFiles( (dir , name) ->
            extensions.stream().anyMatch(name::endsWith));
        return listOfFiles == null ? List.of() : Arrays.stream(listOfFiles).map(File::getName).toList();
    }


    public String getFileType(Resource resource){
        try{
            String type = Files.probeContentType(resource.getFile().toPath());
            return type==null ? "application/text" : type;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSupportedContentType(MultipartFile file){
        String contentType = file.getContentType();
        List<String> supportFileTypes = Arrays.stream(fileStorageProperties.getSupportFileTypes()).toList();
        return supportFileTypes.contains(contentType);
    }

    public List<String> store(List<MultipartFile> files , Integer saleId){
        int order = 1 ;
        List<String> fileNames = new ArrayList<>(files.size());
        for (MultipartFile file : files){
            fileNames.add(store(file,saleId,order));
            order++;
        }
        return fileNames;
    }

    public void removeFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            if (Files.exists(filePath)) {
                Picture picture = pictureRepository.findPictureByRealName(filename);
                pictureRepository.deleteById(picture.getId());
                Files.delete(filePath);
            } else {
                throw new RuntimeException("File: " + filename + " not found!");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Can't remove file: " + filename + ex);
        }
    }

}
