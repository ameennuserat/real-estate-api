package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${image.directory}")
    private String uploadImageDirectory;

    @Override
    public boolean checkIsFound(String path) {
        return Files.exists(Path.of(uploadImageDirectory + "/" + path));
    }

    @Override
    public String uploadImage(MultipartFile image) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

        Path uploadPath = Path.of(uploadImageDirectory);
        Path filePath = uploadPath.resolve(uniqueFileName);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @Override
    public byte[] viewImage(String imageName) throws IOException {
        Path imagePath = Path.of(uploadImageDirectory, imageName);

        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            return null;
        }
    }

    @Override
    public String deleteImage(String imageName) throws IOException {
        Path imagePath = Path.of(uploadImageDirectory, imageName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed";
        }
    }
}
