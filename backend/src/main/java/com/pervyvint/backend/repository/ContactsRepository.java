package com.pervyvint.backend.repository;

import com.pervyvint.backend.entity.ContactsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactsRepository extends JpaRepository<ContactsEntity, Long> {

}
