package com.pervyvint.backend.dto.work;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record WorkRequest (
        @NotBlank(message = "Загрузите фотографию")
        @Size(max = 500)
        @Pattern(
                regexp = "^/media/[0-9a-fA-F-]{36}\\.(?i:jpg|jpeg|png|webp)$",
                message = "Некорректный путь к фото"
        )
        String imageUrl,

        @NotBlank(message = "Укажите заголовок")
        @Size(max = 255)
        String title,

        @NotBlank(message = "Укажите описание")
        @Size(max = 500)
        String description,

        @NotNull(message = "Укажите дату")
        LocalDate date
) {
}
