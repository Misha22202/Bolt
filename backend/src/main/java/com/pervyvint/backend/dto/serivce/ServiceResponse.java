package com.pervyvint.backend.dto.serivce;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        String price,
        String icon,
        Integer sortOrder,
        Boolean isVisible
) {}
