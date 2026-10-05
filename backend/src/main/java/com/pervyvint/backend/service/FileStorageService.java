package com.pervyvint.backend.service;

import com.pervyvint.backend.config.StorageProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "image/webp"
    );

    private final Path uploadDirectory;

    public FileStorageService(StorageProperties storageProperties) {
        this.uploadDirectory = Path.of(storageProperties.getUploadDir())
                .toAbsolutePath()
                .normalize();
    }

    @PostConstruct
    public void initializeStorage() {
        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Не удалось создать папку для загрузок: " + uploadDirectory,
                    exception
            );
        }
    }

    public StoredFile storeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения не передан");
        }

        String contentType = file.getContentType();

        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Возможно сохранять только изображения с расширениями .JPEG, .PNG, .WEBP"
            );
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "" : file.getOriginalFilename()
        );

        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("Некорректное имя файла");
        }

        String extension = getExtension(originalFilename, contentType);
        String storedFilename = UUID.randomUUID() + "." + extension;
        Path targetPath = uploadDirectory.resolve(storedFilename).normalize();

        if (!targetPath.startsWith(uploadDirectory)) {
            throw new IllegalArgumentException("Недопустимый путь к файлу");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException("Не удалось сохранить изображение", exception);
        }

        return new StoredFile(
                originalFilename,
                storedFilename,
                contentType,
                file.getSize(),
                "/media/" + storedFilename
        );
    }

    private String getExtension(String originalFilename, String contentType) {
        int dotIndex = originalFilename.lastIndexOf('.');

        if (dotIndex >= 0 && dotIndex < originalFilename.length() - 1) {
            return originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new IllegalArgumentException("Не удалось определить расширение файла");
        };
    }

    public record StoredFile(
            String originalFilename,
            String storedFilename,
            String contentType,
            long sizeBytes,
            String publicUrl
    ) {
    }
}
