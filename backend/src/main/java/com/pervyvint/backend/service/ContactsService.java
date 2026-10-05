package com.pervyvint.backend.service;

import com.pervyvint.backend.dto.contacts.ContactsRequest;
import com.pervyvint.backend.dto.contacts.ContactsResponse;
import com.pervyvint.backend.dto.contacts.WorkingHourStringDto;
import com.pervyvint.backend.entity.ContactsEntity;
import com.pervyvint.backend.repository.ContactsRepository;
import com.pervyvint.backend.entity.WokringHourString;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactsService {

    private static final Long CONTACTS_ROW_ID = 1L;
    private final ContactsRepository contactsRepository;

    public ContactsService(ContactsRepository contactsRepository){
        this.contactsRepository = contactsRepository;
    }

    @Transactional(readOnly = true)
    public ContactsResponse getContacts() {
        return toResponse(loadContacts());
    }

    public ContactsResponse updateContacts(ContactsRequest request) {
        ContactsEntity entity = loadContacts();

        entity.setAddress(request.address().trim());
        entity.setPhone(request.phone().trim());
        entity.setTelegramUrl(blankToNull(request.telegramUrl()));
        entity.setWorkingHours(toEntityList(request.workingHours()));
        entity.setContactsInfoNote(blankToNull(request.contactsInfoNote()));

        return toResponse(entity);
    }

    private ContactsEntity loadContacts() {
        return contactsRepository.findById(CONTACTS_ROW_ID)
                .orElseThrow(() ->
                        new EntityNotFoundException("Информация о контактах не найдена"
                ));
    }

    private List<WokringHourString> toEntityList(List<WorkingHourStringDto> lines) {
        return lines.stream()
                .map(line -> new WokringHourString(
                        line.day().trim(),
                        line.time().trim()
                ))
                .toList();
    }

    private List<WorkingHourStringDto> toDtoList(List<WokringHourString> lines) {
        return lines.stream()
                .map(line -> new WorkingHourStringDto(
                        line.getDay(),
                        line.getTime()
                ))
                .toList();
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private ContactsResponse toResponse(ContactsEntity entity) {
        return new ContactsResponse(
                entity.getPhone(),
                entity.getAddress(),
                entity.getTelegramUrl(),
                toDtoList(entity.getWorkingHours()),
                entity.getContactsInfoNote()
        );
    }
}
