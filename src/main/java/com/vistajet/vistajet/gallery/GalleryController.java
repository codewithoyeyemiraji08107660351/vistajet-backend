package com.vistajet.vistajet.gallery;

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
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping(value = "/add-gallery", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addGallery(
            @RequestPart("data") @Valid GalleryRequest request,
            @RequestPart("file") MultipartFile file) {

       galleryService.addGallery(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Gallery saved successfully");
    }

    @GetMapping("/galleries")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<GalleryResponse>> getAllGallery(){
        return ResponseEntity.ok(galleryService.getAllGallery());
    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<GalleryResponse>> getAGallery(
            @RequestParam (required = true) List<String> category
    ) {
        return ResponseEntity.ok(galleryService.getAGallery(category));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
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
    public ResponseEntity<?> deleteGallery(@PathVariable Integer id) {
        galleryService.deleteGallery(id);
        return ResponseEntity.ok("Gallery removed successfully");
    }

}
