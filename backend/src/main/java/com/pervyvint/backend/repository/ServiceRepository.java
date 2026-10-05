package com.pervyvint.backend.repository;

import com.pervyvint.backend.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findAllByIsVisibleTrueOrderBySortOrderAsc();
}
