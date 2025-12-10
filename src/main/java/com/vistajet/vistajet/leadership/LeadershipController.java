package com.vistajet.vistajet.leadership;

import com.vistajet.vistajet.gallery.GalleryResponse;
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
@RequestMapping("/api/v1/leadership")
@Validated
@Tag(name = "Leadership")
public class LeadershipController {

    private final LeadershipService leadershipService;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Register Leadership",
            description = "Adds a new Leadership entry. Accessible only to Admin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Leadership created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LeadershipResponse.class)
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
    public ResponseEntity<?> registerLeader(
            @RequestPart("data") @Valid LeadershipRequest request,
            @RequestPart("file") MultipartFile file) {

        leadershipService.register(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Leadership created successfully");
    }

    @GetMapping("/leaders")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get All Leaders",
            description = "Retrieve all Leadership entries."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of leaders retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LeadershipResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<LeadershipResponse>> getAllLeader() {
        return ResponseEntity.ok(leadershipService.getAllLeaders());
    }

    @GetMapping("/leader/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Find Leader",
            description = "Retrieve a Leadership entry by ID or full name. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Leader retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LeadershipResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Leader not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<LeadershipResponse> getLeader(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String fullName
    ) {
        return ResponseEntity.ok(leadershipService.getALeadership(id, fullName));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Update Leadership",
            description = "Update an existing Leadership entry. File is optional."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Leadership updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LeadershipResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or file",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Leader not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> updateLeader(
            @PathVariable Integer id,
            @RequestPart("data") @Valid LeadershipRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        leadershipService.update(id, request, file);
        return ResponseEntity.ok("Leadership updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Delete Leadership",
            description = "Delete a Leadership entry by ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Leadership removed successfully",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Leader not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> deleteLeader(@PathVariable Integer id) {
        leadershipService.delete(id);
        return ResponseEntity.ok("Leadership removed successfully");
    }
}
