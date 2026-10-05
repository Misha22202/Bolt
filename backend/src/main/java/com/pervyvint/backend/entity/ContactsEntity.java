package com.pervyvint.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contacts")
public class ContactsEntity {

    @Id
    private Long id = 1L;

    @Column(nullable = false, length = 40)
    private String phone;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "telegram_url", length = 255)
    private String telegramUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "working_hours", nullable = false, columnDefinition = "jsonb")
    private List<WokringHourString> workingHours = new ArrayList<>();

    @Column(name = "contacts_info_note", length = 500)
    private String contactsInfoNote;

    @Column(name = "updated_at", nullable = false, length = 255)
    private OffsetDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public List<WokringHourString> getWorkingHours() {
        return workingHours;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTelegramUrl(String telegramUrl) {
        this.telegramUrl = telegramUrl;
    }

    public void setWorkingHours(List<WokringHourString> workingHours) {
        this.workingHours = workingHours;
    }

    public String getContactsInfoNote() {
        return contactsInfoNote;
    }

    public void setContactsInfoNote(String contactsInfoNote) {
        this.contactsInfoNote = contactsInfoNote;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
