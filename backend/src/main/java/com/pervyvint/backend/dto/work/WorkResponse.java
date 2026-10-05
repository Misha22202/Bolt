package com.pervyvint.backend.dto.work;

import java.time.LocalDate;

public record WorkResponse (
        Long id,
        String imageUrl,
        String title,
        String description,
        LocalDate date
) {
}
