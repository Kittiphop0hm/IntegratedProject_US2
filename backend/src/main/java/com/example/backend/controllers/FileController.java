package com.example.backend.controllers;

import com.example.backend.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        fileService.store(file);
        return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded " + file.getOriginalFilename());
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.loadFileAsResource(filename);
        return ResponseEntity.ok().contentType(MediaType.valueOf(fileService.getFileType(file))).body(file);
    }

//    @PostMapping("")
//    public ResponseEntity<String> uploadFiles(@RequestParam("file") List<MultipartFile> files) {
//        fileService.store(files);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Files are uploaded" + files);
//    }

    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<Object> removeFile(@PathVariable String filename) {
        fileService.removeFile(filename);
        return ResponseEntity.ok("File: " + filename + " removed");
    }
}
