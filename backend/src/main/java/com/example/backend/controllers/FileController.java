package com.example.backend.controllers;

import com.example.backend.dtos.pictures.ResponsePictureDto;
import com.example.backend.entities.Picture;
import com.example.backend.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class FileController {
    @Autowired
    private FileService fileService;

//    @PostMapping("")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        fileService.store(file);
//        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded " + file.getOriginalFilename());
//    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);
        return ResponseEntity.ok().contentType(MediaType.valueOf(fileService.getFileType(file))).body(file);
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> getImagesList() {
        return ResponseEntity.ok().body(fileService.getImageList());
    }

    @GetMapping("/imageSale/{id}")
    public ResponseEntity<List<ResponsePictureDto>> getImagesBySaleId(@PathVariable Integer id) {
        return ResponseEntity.ok(fileService.findPicturesBySaleItemId(id));
    }

    @PostMapping("")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files ,@RequestParam("saleId") Integer saleId) {
        fileService.store(files , saleId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Files are uploaded. " + files);
    }
}
