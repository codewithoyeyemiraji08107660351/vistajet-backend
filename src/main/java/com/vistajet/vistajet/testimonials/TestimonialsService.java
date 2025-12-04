package com.vistajet.vistajet.testimonials;

import com.vistajet.vistajet.exceptions.InvalidRequestException;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import com.vistajet.vistajet.file.TestimonialImageStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestimonialsService {

    private final TestimonialsRepository repository;
    private final TestimonialImageStorage storage;

    public void createTestimonials(TestimonialsRequest request, MultipartFile file) {
        String fileName = storage.saveTestimonialsPhoto(file);
        Testimonials testimonials = Testimonials.builder()
                .name(request.getName())
                .role(request.getRole())
                .message(request.getMessage())
                .image(fileName)
                .build();

        repository.save(testimonials);
    }

    public List<TestimonialsResponse> getAllTestimonials() {
        List<Testimonials> testimonials = repository.findAll();

                return testimonials
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public TestimonialsResponse getATestimonials(Integer id, String fullName) {

        Testimonials testimonials;
        if(id != null){
            testimonials = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Testimonials with ID " + id + " not found"));
        }else if(fullName != null){
            testimonials = repository.findByNameIgnoreCase(fullName)
                    .orElseThrow(() -> new ResourceNotFoundException("Testimonials with ID" + fullName + " not found"));
        }else{
            throw new InvalidRequestException("Please provide id or fullName");
        }
        return toResponse(testimonials);
    }

    public void updateTestimonials(Integer id, @Valid TestimonialsResponse request, MultipartFile file) {

        Testimonials testimonials = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update Testimonials with ID " + id + " not found"));

        testimonials.setName(request.getName());
        testimonials.setRole(request.getRole());
        testimonials.setMessage(request.getMessage());

        if (file != null && !file.isEmpty()) {
            String filename = storage.saveTestimonialsPhoto(file);
          testimonials.setImage(filename);
        }

        repository.save(testimonials);
    }

    @Transactional
    public void deleteTestimonials(Integer id) {
        Testimonials testimonial = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Testimonial with ID " + id + " does not exist"
                ));

        String image = testimonial.getImage();

        repository.delete(testimonial);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (image == null || image.isBlank()) {
                    log.warn("No testimonial image to delete for ID {}", id);
                    return;
                }

                try {
                    storage.deleteTestimonialsPhoto(image);
//                    log.info("Testimonial image '{}' deleted successfully", image);
                } catch (Exception e) {
                    log.error("Failed to delete testimonial image '{}' after DB commit", image, e);
                }
            }
        });
    }


    private TestimonialsResponse toResponse(Testimonials testimonials) {
        return TestimonialsResponse.builder()
                .id(testimonials.getId())
                .name(testimonials.getName())
                .role(testimonials.getRole())
                .message(testimonials.getMessage())
                .image(storage.loadTestimonialsPhoto(testimonials.getImage()))
                .createdAt(testimonials.getCreatedAt())
                .build();
    }

}
