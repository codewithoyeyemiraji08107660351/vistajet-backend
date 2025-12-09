package com.vistajet.vistajet.about;

import com.vistajet.vistajet.user.Users;
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
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/about")
@Tag(name = "About")
public class AboutController {

    private final AboutService service;

    @PostMapping(value = "/add-about")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Add About Information",
            description = "Adds a new About entry. Accessible to authenticated users."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "About saved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AboutResponse.class)
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
    public ResponseEntity<?> addAbout(
            @RequestBody @Valid AboutRequest request) {
        service.createAbout(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("About saved successfully");
    }

    @GetMapping("/all-about")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get All About Entries",
            description = "Returns a list of all About entries."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of About entries retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AboutResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<AboutResponse>> getAllAbout() {

        return ResponseEntity.ok(service.getAllAbout());
    }

    @Operation(
            summary = "Update About Information",
            description = "Updates an existing About entry by ID. Accessible to authenticated users."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "About updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AboutResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "About entry not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    @PutMapping(value = "/update/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateAbout(@PathVariable("id") Integer id,
                                         @RequestBody AboutRequest request){
        service.updateAbout(id, request);
        return ResponseEntity.ok("About updated successfully");
    }


}

