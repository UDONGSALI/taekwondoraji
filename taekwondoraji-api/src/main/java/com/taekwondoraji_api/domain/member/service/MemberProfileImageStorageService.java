package com.taekwondoraji_api.domain.member.service;

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
public class MemberProfileImageStorageService {

    private static final List<String> SUPPORTED_EXTENSIONS = List.of("jpg", "jpeg", "png", "webp");
    private static final Path UPLOAD_DIRECTORY = Paths.get("uploads", "member-profiles");

    public void save(Integer memberId, MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return;
        }

        String extension = getExtension(imageFile.getOriginalFilename());
        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        try {
            Files.createDirectories(UPLOAD_DIRECTORY);
            delete(memberId);
            imageFile.transferTo(UPLOAD_DIRECTORY.resolve(memberId + "." + extension));
        } catch (IOException exception) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void delete(Integer memberId) {
        for (String extension : SUPPORTED_EXTENSIONS) {
            try {
                Files.deleteIfExists(UPLOAD_DIRECTORY.resolve(memberId + "." + extension));
            } catch (IOException exception) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public String findImageUrl(Integer memberId) {
        for (String extension : SUPPORTED_EXTENSIONS) {
            Path imagePath = UPLOAD_DIRECTORY.resolve(memberId + "." + extension);
            if (Files.exists(imagePath)) {
                return "/uploads/member-profiles/" + memberId + "." + extension;
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
