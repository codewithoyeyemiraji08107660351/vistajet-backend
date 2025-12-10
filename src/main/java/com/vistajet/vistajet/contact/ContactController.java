package com.vistajet.vistajet.contact;


import com.vistajet.vistajet.about.AboutResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact")
@CrossOrigin(origins ="https://customshangarservices.vercel.app/")
@Tag(name = "Contacts")
public class ContactController {

    private final ContactService contactService;

    @PostMapping(value = "/add-contact")
    @Operation(
            summary = "Add Contact Information",
            description = "Adds a new Contact entry. Accessible to all users."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Contact saved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContactResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> addContact(
            @RequestBody @Valid ContactRequest request) {
        contactService.createContact(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Contact saved successfully");
    }

    @GetMapping("/all-contact")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Get All Contact Entries",
            description = "Returns a list of all Contact entries. Requires authentication as it only accessible to only Admin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of contacts retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ContactResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContact());
    }

}
