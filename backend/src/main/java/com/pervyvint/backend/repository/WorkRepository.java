package com.pervyvint.backend.repository;

import com.pervyvint.backend.entity.WorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkRepository extends JpaRepository<WorkEntity, Long> {

    List<WorkEntity> findAllByOrderByDateDescIdDesc();
}
