package com.pervyvint.backend.controller.publicapi;

import com.pervyvint.backend.dto.serivce.ServiceResponse;
import com.pervyvint.backend.service.ServiceCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicServiceController {

    private final ServiceCatalogService serviceCatalogService;

    public PublicServiceController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @GetMapping("/services")
    public List<ServiceResponse> getAll() {
        return serviceCatalogService.getPublicServices();
    }
}
