package com.vistajet.vistajet.gallery;

import com.vistajet.vistajet.contact.ContactResponse;
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
@RequestMapping("/api/v1/gallery")
@Validated
@Tag(name = "Galleries")
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping(value = "/add-gallery", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Add Gallery Details",
            description = "Adds a new Gallery entry. Accessible only to Admin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Gallery saved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GalleryResponse.class)
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
    public ResponseEntity<?> addGallery(
            @RequestPart("data") @Valid GalleryRequest request,
            @RequestPart("file") MultipartFile file) {

       galleryService.addGallery(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Gallery saved successfully");
    }

    @GetMapping("/galleries")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get All Galleries",
            description = "Retrieve a list of all gallery entries."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of galleries retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GalleryResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<GalleryResponse>> getAllGallery(){
        return ResponseEntity.ok(galleryService.getAllGallery());
    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get Galleries By Category",
            description = "Retrieve gallery entries filtered by categories. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Filtered galleries retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GalleryResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<GalleryResponse>> getAGallery(
            @RequestParam (required = true) List<String> category
    ) {
        return ResponseEntity.ok(galleryService.getAGallery(category));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Update Gallery Entry",
            description = "Update an existing gallery entry. File is optional."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Gallery updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GalleryResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or file",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Gallery entry not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> updateGallery(
            @PathVariable Integer id,
            @RequestPart("data") @Valid GalleryRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        galleryService.updateGallery(id, request, file);
        return ResponseEntity.ok("Gallery updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Delete Gallery Entry",
            description = "Deletes a gallery entry by ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Gallery removed successfully",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Gallery entry not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> deleteGallery(@PathVariable Integer id) {
        galleryService.deleteGallery(id);
        return ResponseEntity.ok("Gallery removed successfully");
    }

}
