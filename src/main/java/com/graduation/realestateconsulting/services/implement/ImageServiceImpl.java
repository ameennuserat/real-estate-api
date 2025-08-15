package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.services.ImageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${image.directory}")
    private String uploadImageDirectory;

    @PostConstruct
    public void init() {
        File file = new File(uploadImageDirectory);
        if (!file.exists()) {
            file.mkdirs();
            System.out.println("Images Directory Created");
        } else {
            System.out.println("Images Directory Exists");
        }
    }


    @Override
    public String uploadImage(MultipartFile image) throws IOException {

        String uniqueFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        String directory = uploadImageDirectory;
        InputStream inputStream = image.getInputStream();

        Path filePath = Path.of(directory, uniqueFileName);

        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @Override
    public String uploadBinary(String fileName, byte[] fileData) throws IOException {
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        String directory = uploadImageDirectory;

        Path filePath = Path.of(directory, uniqueFileName);
        Files.write(filePath, fileData);

        // هنا يمكنك إرجاع رابط الوصول إلى الملف
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
