package com.vistajet.vistajet.service;

import com.vistajet.vistajet.partners.PartnerResponse;
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
@RequestMapping("/api/v1/service")
@Validated
@Tag(name = "Services")
public class ServiceController {

    private final MyServices service;

    @PostMapping(value = "/add-service", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create Service",
            description = "Adds a new Service entry. Accessible only to Admin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Service created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)
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
    public ResponseEntity<?> addService(
            @RequestPart("data") @Valid ServiceRequest request,
            @RequestPart("file") MultipartFile file) {
        service.createService(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Service saved successfully");
    }


    @GetMapping("/all-service")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get All Services",
            description = "Retrieve a list of all services."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of services retrieved successfully",
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
    public ResponseEntity<List<ServiceResponse>> getAllService(){
        return ResponseEntity.ok(service.getAllService());

    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Find Service",
            description = "Retrieve a service by ID or title. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Service retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<ServiceResponse> getAService(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok(service.getAService(id, title));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Update Service",
            description = "Updates an existing service. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Service updated successfully",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or file",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> updateService(
            @PathVariable Integer id,
            @RequestPart("data") @Valid ServiceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        service.updateService(id, request, file);
        return ResponseEntity.ok("Service updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Delete Service",
            description = "Deletes a service by ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Service removed successfully",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> deleteService(@PathVariable Integer id) {
        service.deleteService(id);
        return ResponseEntity.ok("Service removed successfully");
    }
}
