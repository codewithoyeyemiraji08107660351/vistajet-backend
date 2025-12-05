package com.vistajet.vistajet.testimonials;

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
@RequestMapping("/api/v1/testimonials")
@CrossOrigin(origins = "https://localhost:5173")
@Validated
public class TestimonialsController {

    private final TestimonialsService testimonialsService;

    @PostMapping(value = "/create-testimonials", consumes = "multipart/form-data")
    public ResponseEntity<?> createTestimonials(
            @RequestPart("data") @Valid TestimonialsRequest request,
            @RequestPart("file") MultipartFile file) {

       testimonialsService.createTestimonials(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Testimonials created successfully");
    }

    @GetMapping("/all-testimonials")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<TestimonialsResponse>> getAllTestimonials() {
        return ResponseEntity.ok( testimonialsService.getAllTestimonials());
    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<TestimonialsResponse> getAllTestimonials(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String fullName
    ) {
        return ResponseEntity.ok(testimonialsService.getATestimonials(id, fullName));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateTestimonials(
            @PathVariable Integer id,
            @RequestPart("data") @Valid TestimonialsResponse request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

       testimonialsService.updateTestimonials(id, request, file);
        return ResponseEntity.ok("Testimonials updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> deleteTestimonials(@PathVariable Integer id) {
        testimonialsService.deleteTestimonials(id);
        return ResponseEntity.ok("Testimonials removed successfully");
    }
}
