package com.vistajet.vistajet.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service")
@CrossOrigin(origins = "https://customshangarservices.netlify.app/")
@Validated
public class ServiceController {

    private final MyServices service;

    @PostMapping(value = "/add-service", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addService(
            @RequestPart("data") @Valid ServiceRequest request,
            @RequestPart("file") MultipartFile file) {
        service.createService(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Service saved successfully");
    }


    @GetMapping("/all-service")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ServiceResponse>> getAllService(){
        return ResponseEntity.ok(service.getAllService());
    }
    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ServiceResponse> getAService(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok(service.getAService(id, title));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestPart("data") @Valid ServiceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        service.updateService(id, request, file);
        return ResponseEntity.ok("Service updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.deleteService(id);
        return ResponseEntity.ok("Service removed successfully");
    }
}
