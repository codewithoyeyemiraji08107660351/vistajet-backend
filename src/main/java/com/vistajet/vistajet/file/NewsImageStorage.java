package com.vistajet.vistajet.file;

import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class NewsImageStorage {


    @Value("${application.file.upload.base-path}")
    private String baseUploadPath;

    @Value("${application.file.max-size}")
    private long maxFileSize;

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png");

    public String saveNewsPhoto(MultipartFile file) {

        validateFile(file);

        String folder = baseUploadPath + File.separator + "news";
        File dir = new File(folder);

        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("Could not create upload directory");
        }

        String ext = getExtension(file.getOriginalFilename());
        String filename = System.currentTimeMillis() + "." + ext;

        Path path = Paths.get(folder, filename);

        try {
            Files.write(path, file.getBytes());
            return filename;
        } catch (IOException e) {
            throw new ResourceNotFoundException("Failed to save image");
        }
    }


    public String loadNewsPhoto(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new RuntimeException("Invalid image filename");
        }
        return "/uploads/news/"+filename;
    }

    private void validateFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("File too large. Max allowed is 10MB.");
        }

        String ext = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXT.contains(ext)) {
            throw new RuntimeException("Invalid image type. Only JPG, JPEG, PNG allowed.");
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf(".");
        return (idx == -1) ? "" : filename.substring(idx + 1).toLowerCase();
    }

    public void deleteNewsPhoto(String filename) {
        try {
            Path path = Paths.get(baseUploadPath, "news", filename);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("File deleted: {}", filename);
            }
        } catch (IOException e) {
            log.warn("Could not delete file: {}", filename, e);
        }
    }
}
