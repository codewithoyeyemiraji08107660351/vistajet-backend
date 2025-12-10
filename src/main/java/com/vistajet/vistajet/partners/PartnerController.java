package com.vistajet.vistajet.partners;

import com.vistajet.vistajet.news.NewsResponse;
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
@RequestMapping("/api/v1/partners")
@CrossOrigin(origins ="https://customshangarservices.vercel.app/")
@Validated
@Tag(name = "Partners")
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping(value = "/create-partner", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create Partner",
            description = "Adds a new Partner entry. Accessible only to Admin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Partner created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PartnerResponse.class)
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
    public ResponseEntity<?> createPartner(
            @RequestPart("data") @Valid PartnerRequest request,
            @RequestPart("file") MultipartFile file) {

        partnerService.createPartner(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Partner created successfully");
    }

    @GetMapping("/all-partners")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get All Partners",
            description = "Retrieve a list of all partners."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of partners retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PartnerResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<PartnerResponse>> getAllPartners() {
        return ResponseEntity.ok(partnerService.getAllPartners());
    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Find Partner",
            description = "Retrieve a partner by ID or company name. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Partner retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PartnerResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Partner not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<PartnerResponse> getAPartner(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String companyName
    ) {
        return ResponseEntity.ok(partnerService.getAPartner(id, companyName));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Delete Partner",
            description = "Delete a partner by ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Partner removed successfully",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Partner not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> deletePartners(@PathVariable Integer id) {
        partnerService.deletePartners(id);
        return ResponseEntity.ok("Partner removed successfully");
    }

}
