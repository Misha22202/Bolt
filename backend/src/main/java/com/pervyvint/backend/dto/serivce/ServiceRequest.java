package com.pervyvint.backend.dto.serivce;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ServiceRequest(
        @NotBlank(message = "Название услуги обязательно")
        @Size(max = 255)
        String name,

        @Size(max = 3000)
        String description,

        @Size(max = 100)
        String price,

        @NotBlank
        @Size(max = 50)
        String icon,

        @NotNull
        Integer sortOrder,

        @NotNull
        Boolean isVisible
) {}
