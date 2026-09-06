package com.clockstore.Clock_Store.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clockstore.Clock_Store.exception.BadRequestException;

@Service
public class FileStorageService {

    private final Path uploadDirectory = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public String store(MultipartFile file, String folder) {

        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null || originalFilename.isBlank()) {
                throw new BadRequestException("Invalid file name");
            }

            String extension = "";

            int extensionIndex = originalFilename.lastIndexOf('.');

            if (extensionIndex >= 0) {
                extension = originalFilename.substring(extensionIndex).toLowerCase();
            }

            String filename = UUID.randomUUID() + extension;

            Path folderPath = uploadDirectory.resolve(folder);

            Files.createDirectories(folderPath);

            Path targetPath = folderPath.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + folder + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    public void delete(String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            String relativePath = fileUrl.startsWith("/")
                    ? fileUrl.substring(1)
                    : fileUrl;

            Path filePath = Paths.get(relativePath);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Could not delete file", e);
        }
    }
}