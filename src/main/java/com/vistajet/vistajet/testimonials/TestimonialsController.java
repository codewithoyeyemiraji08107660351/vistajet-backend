package com.vistajet.vistajet.testimonials;

import com.vistajet.vistajet.service.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin(origins ="https://customshangarservices.vercel.app/")
@Validated
@Tag(name = "Testimonials")
public class TestimonialsController {

    private final TestimonialsService testimonialsService;

    @PostMapping(value = "/create-testimonials", consumes = "multipart/form-data")
    @Operation(
            summary = "Create Testimonials",
            description = "Adds a new Testimonials entry. Accessible only to users."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Testimonials created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TestimonialsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or file",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> createTestimonials(
            @RequestPart("data") @Valid TestimonialsRequest request,
            @RequestPart("file") MultipartFile file) {

       testimonialsService.createTestimonials(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Testimonials created successfully");
    }

    @GetMapping("/all-testimonials")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get All Testimonials",
            description = "Retrieve a list of all testimonials."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of testimonials retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ServiceResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<TestimonialsResponse>> getAllTestimonials() {
        return ResponseEntity.ok( testimonialsService.getAllTestimonials());
    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Find testimonials",
            description = "Retrieve a testimonials by ID or by full name. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Testimonials retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Testimonials not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<TestimonialsResponse> getATestimonials(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String fullName
    ) {
        return ResponseEntity.ok(testimonialsService.getATestimonials(id, fullName));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Update Testimonials",
            description = "Updates an existing Testimonials entry. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Testimonials updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TestimonialsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or file",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Testimonials not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
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
    @Operation(
            summary = "Delete Testimonials",
            description = "Deletes a Testimonials entry by ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Testimonials removed successfully",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Testimonials not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> deleteTestimonials(@PathVariable Integer id) {
        testimonialsService.deleteTestimonials(id);
        return ResponseEntity.ok("Testimonials removed successfully");
    }
}
