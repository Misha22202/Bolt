package com.pervyvint.backend.controller.publicapi;

import com.pervyvint.backend.dto.contacts.ContactsResponse;
import com.pervyvint.backend.service.ContactsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicContactsController {

    private final ContactsService contactsService;

    public PublicContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping("/contacts")
    public ContactsResponse getContacts() {
        return contactsService.getContacts();
    }
}
