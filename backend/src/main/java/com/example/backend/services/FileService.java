package com.example.backend.services;

import com.example.backend.utils.FileStorageProperties;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileService {
    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;
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

    public String store(MultipartFile file){
        if(!isSupportedContentType(file)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The content type of the file is not supported." + file.getContentType());
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream() , targetLocation , StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName, e);
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

    public List<String> store(List<MultipartFile> files){
        List<String> fileNames = new ArrayList<>(files.size());
        files.forEach(file -> fileNames.add(store(file)));
        return fileNames;
    }

    public void removeFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("File: " + filename + " not found!");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Can't remove file: " + filename + ex);
        }

    }

}
