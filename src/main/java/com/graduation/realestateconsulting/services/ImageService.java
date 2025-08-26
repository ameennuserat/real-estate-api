package com.graduation.realestateconsulting.services;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String uploadImage(MultipartFile image) throws IOException;

    public String uploadBinary(String fileName, byte[] fileData) throws IOException;

    byte[] viewImage(String imageName) throws IOException;

    String deleteImage(String imageName) throws IOException;

}