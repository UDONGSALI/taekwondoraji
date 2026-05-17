package com.taekwondoraji_api.domain.gym.web.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

@Service
public class PointItemImageStorageService {

    private static final List<String> SUPPORTED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp");
    private static final Path UPLOAD_DIRECTORY = Paths.get("uploads", "gym-point-items");

    public void save(Integer gymPointItemId, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return;
        }

        String extension = getExtension(imageFile.getOriginalFilename());
        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        try {
            Files.createDirectories(UPLOAD_DIRECTORY);
            delete(gymPointItemId);
            imageFile.transferTo(UPLOAD_DIRECTORY.resolve(gymPointItemId + "." + extension));
        } catch (IOException exception) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void delete(Integer gymPointItemId) {
        for (String extension : SUPPORTED_EXTENSIONS) {
            try {
                Files.deleteIfExists(UPLOAD_DIRECTORY.resolve(gymPointItemId + "." + extension));
            } catch (IOException exception) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public String findImageUrl(Integer gymPointItemId) {
        for (String extension : SUPPORTED_EXTENSIONS) {
            Path imagePath = UPLOAD_DIRECTORY.resolve(gymPointItemId + "." + extension);
            if (Files.exists(imagePath)) {
                return "/uploads/gym-point-items/" + gymPointItemId + "." + extension;
            }
        }

        return null;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}
