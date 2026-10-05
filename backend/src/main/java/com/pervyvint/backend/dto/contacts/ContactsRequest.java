package com.pervyvint.backend.dto.contacts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ContactsRequest (
        @NotBlank(message = "Укажите номер телефона")
        @Size(max = 40)
        String phone,

        @NotBlank(message = "Укажите адрес")
        @Size(max = 255)
        String address,

        @Size(max = 255)
        String telegramUrl,

        @NotEmpty(message = "Укажите график работы")
        List<@Valid WorkingHourStringDto> workingHours,

        @Size(max = 500)
        String contactsInfoNote
) {
}
