package com.vistajet.vistajet.service;

import com.vistajet.vistajet.exceptions.InvalidRequestException;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import com.vistajet.vistajet.file.ServiceImagesStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyServices {

    private final ServiceRepository repository;
    private final ServiceImagesStorage storage;


    public void createService(ServiceRequest request, MultipartFile file) {

        String serviceImage = storage.saveServiceImage(file);

        VistajetService vista = VistajetService.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .features(request.getFeatures())
                .image(serviceImage)
                .build();
        repository.save(vista);

    }

    public List<ServiceResponse> getAllService() {
        List<VistajetService> list = repository.findAll();

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No Service record found");
        }
        return list.stream()
                .map(this::toResponse)
                .toList();
    }

    public ServiceResponse getAService(Integer id, String title) {

        VistajetService vistajetService;
        if (id != null) {
           vistajetService = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Service with ID: " + id + " not found"));
        } else if (title != null && !title.isBlank()) {
            vistajetService = repository.findByTitleIgnoreCase(title)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Service with title: '" + title + "' not found"));
        } else {
            throw new InvalidRequestException("Please provide id or title");
        }

        return toResponse(vistajetService);
    }


    public void updateService(Integer id, ServiceRequest request, MultipartFile file) {
       VistajetService vista = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update Service with ID " + id + " not found"));

       vista.setTitle(request.getTitle());
       vista.setDescription(request.getDescription());
       vista.setFeatures(request.getFeatures());

        if (file != null && !file.isEmpty()) {
            String filename = storage.saveServiceImage(file);
            vista.setImage(filename);
        }

        repository.save(vista);
    }


    public void deleteService(Integer id) {
       VistajetService vista = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Service with ID " + id + " does not exist"
                ));

        String serviceImage = vista.getImage();

        repository.delete(vista);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                   storage.deleteServiceImage(serviceImage);
                } catch (Exception e) {
                    log.error("Failed to delete file '{}' after leader delete commit", serviceImage, e);
                }
            }
        });
    }

    public ServiceResponse toResponse(VistajetService response){
        return ServiceResponse.builder()
                .id(response.getId())
                .title(response.getTitle())
                .description(response.getDescription())
                .features(response.getFeatures())
                .image(storage.loadServiceImage(response.getImage()))
                .build();
    }

}
