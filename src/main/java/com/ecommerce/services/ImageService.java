package com.ecommerce.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    public String saveImage(MultipartFile file) throws IOException {
        String folder = "src/main/resources/static/images/";
        Path folderPath = Paths.get(folder);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = folderPath.resolve(fileName);
        Files.write(path, bytes);
        return fileName;
    }

    public void deleteImage(String fileName) {
        try {
            Path filePath = Paths.get("src/main/resources/static/images/" + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Failed to delete old image: " + e.getMessage());

        }
    }
}
