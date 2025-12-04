package com.vistajet.vistajet.contact;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContact());
    }

}
