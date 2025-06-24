package com.graduation.realestateconsulting.controller;


import com.graduation.realestateconsulting.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/images")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) throws IOException {
        MediaType mediaType = imageName.endsWith(".png") ? MediaType.IMAGE_PNG: MediaType.IMAGE_JPEG;
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageService.viewImage(imageName));
    }

}
