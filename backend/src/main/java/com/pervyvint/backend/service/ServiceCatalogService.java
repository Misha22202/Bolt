package com.pervyvint.backend.service;

import com.pervyvint.backend.dto.serivce.ServiceRequest;
import com.pervyvint.backend.dto.serivce.ServiceResponse;
import com.pervyvint.backend.entity.ServiceEntity;
import com.pervyvint.backend.repository.ServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServiceCatalogService {

    private final ServiceRepository repository;

    public ServiceCatalogService(ServiceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> getPublicServices() {
        return repository.findAllByIsVisibleTrueOrderBySortOrderAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServices() {
        return repository.findAll()
                .stream()
                .sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
                .map(this::toResponse)
                .toList();
    }

    public ServiceResponse create(ServiceRequest request) {
        ServiceEntity entity = new ServiceEntity();
        applyRequest(entity, request);

        return toResponse(repository.save(entity));
    }

    public ServiceResponse update(Long id, ServiceRequest request) {
        ServiceEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Услуга не найдена: " + id));

        applyRequest(entity, request);

        return toResponse(entity);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Услуга не найдена: " + id);
        }

        repository.deleteById(id);
    }

    private void applyRequest(ServiceEntity entity, ServiceRequest request) {
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        entity.setIcon(request.icon());
        entity.setSortOrder(request.sortOrder());
        entity.setVisible(request.isVisible());
    }

    private ServiceResponse toResponse(ServiceEntity entity) {
        return new ServiceResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getIcon(),
                entity.getSortOrder(),
                entity.getVisible()
        );
    }
}
