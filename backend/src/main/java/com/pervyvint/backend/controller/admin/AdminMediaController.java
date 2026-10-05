package com.pervyvint.backend.controller.admin;

import com.pervyvint.backend.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/media")
public class AdminMediaController {

    private final FileStorageService fileStorageService;

    public AdminMediaController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileStorageService.StoredFile upload(
            @RequestParam("file") MultipartFile file
            ) {
        return fileStorageService.storeImage(file);
    }
}
