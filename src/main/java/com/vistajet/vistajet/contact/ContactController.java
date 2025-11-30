package com.vistajet.vistajet.contact;


import com.vistajet.vistajet.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    @PostMapping(value = "/add-contact")
    public ResponseEntity<?> addGallery(
            @RequestBody @Valid ContactRequest request) {
        contactService.createContact(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Contact saved successfully");
    }

    @GetMapping("/all-contact")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PageResponse<ContactResponse>> getAllGallery(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(contactService.getAllContact(page, size));
    }

}
