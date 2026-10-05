package com.pervyvint.backend.dto.contacts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorkingHourStringDto(
        @NotBlank(message = "Укажите день или диапазон дней")
        @Size(max = 100)
        String day,

        @NotBlank(message = "Укажите время работы")
        @Size(max = 100)
        String time
) {
}
