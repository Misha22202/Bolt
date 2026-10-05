package com.pervyvint.backend.controller.admin;

import com.pervyvint.backend.dto.contacts.ContactsRequest;
import com.pervyvint.backend.dto.contacts.ContactsResponse;
import com.pervyvint.backend.service.ContactsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contacts")
public class AdminContactsController {

    private final ContactsService contactsService;

    public AdminContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping
    public ContactsResponse getContacts() {
        return contactsService.getContacts();
    }

    @PutMapping
    public ContactsResponse updateContacts(
            @Valid @RequestBody ContactsRequest request
            ) {
        return contactsService.updateContacts(request);
    }
}
