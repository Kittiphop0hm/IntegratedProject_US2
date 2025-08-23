package com.example.backend.services.users;

import com.example.backend.dtos.files.ListFilesDto;
import com.example.backend.entities.User;
import com.example.backend.entities.Userpicture;
import com.example.backend.repositories.UserPictureRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utils.FileStorageProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserFileService {
    private final Path userFileStorageLocation;
    private final FileStorageProperties userFileStorageProperties;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPictureRepository userPictureRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserFileService(FileStorageProperties fileStorageProperties) {
        this.userFileStorageProperties = fileStorageProperties;
        this.userFileStorageLocation = Paths.get(fileStorageProperties.getUploadNational()).toAbsolutePath().normalize();
        try {
            if (!Files.exists(this.userFileStorageLocation)) {
                Files.createDirectories(this.userFileStorageLocation);
            }
        } catch(IOException e){
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String getPath() {
        return "Hello" + this.userFileStorageLocation;
    }

    private boolean isSupportedContentType(MultipartFile file){
        String contentType = file.getContentType();
        List<String> supportFileTypes = Arrays.stream(userFileStorageProperties.getSupportFileTypes()).toList();
        return supportFileTypes.contains(contentType);
    }

    public ListFilesDto store(MultipartFile file, Integer userId, Integer order) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id: " + userId + " not found!"));
        if (!isSupportedContentType(file)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The content type of the file is not supported." + file.getContentType());
        String originName = StringUtils.cleanPath(file.getOriginalFilename());

        String extension = originName.substring(originName.lastIndexOf("."));
        String newFilename = userId + "." + order + extension;
        try {
            Path targetLocation = this.userFileStorageLocation.resolve(newFilename);
            Userpicture userpicture = new Userpicture();
            userpicture.setFileName(newFilename);
            userpicture.setImageViewOrder(order);
            userpicture.setUsers(user);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            userPictureRepository.save(userpicture);
            return modelMapper.map(userpicture, ListFilesDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + originName, e);
        }
    }

    public List<ListFilesDto> storeList(List<MultipartFile> files, Integer userId) {
        int order = 1;
        List<ListFilesDto> fileList = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            fileList.add(store(file, userId, order));
            order++;
        }
        return fileList;
    }

    public List<Userpicture> findPictureByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User id: " + userId + " not found!"));
        return userPictureRepository.findUserpicturesByUsers_Id(user.getId());

    }

}
