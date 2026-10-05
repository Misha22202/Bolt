package com.pervyvint.backend.dto.contacts;

import java.util.List;

public record ContactsResponse(
        String phone,
        String address,
        String telegramUrl,
        List<WorkingHourStringDto> workingHours,
        String contactsInfoNote
) {
}
