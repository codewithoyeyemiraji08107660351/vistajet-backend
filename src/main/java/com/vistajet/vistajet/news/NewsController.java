package com.vistajet.vistajet.news;

import com.vistajet.vistajet.leadership.LeadershipResponse;
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
@RequestMapping("/api/v1/news")
@CrossOrigin(origins = "https://customshangarservices.vercel.app")
@Validated
@Tag(name = "News")
public class NewsController {

    private final NewsService service;

    @PostMapping(value = "/create-news", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Create News",
            description = "Adds a new News entry. Accessible only to Admin."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "News created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class)
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
    public ResponseEntity<?> createNews(
            @RequestPart("data") @Valid NewsRequest request,
            @RequestPart("file") MultipartFile file) {

        service.createNews(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("News created successfully");
    }

    @GetMapping("/all-news")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Get All News",
            description = "Retrieve a list of all news entries."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of news retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NewsResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<List<NewsResponse>> getAllNews() {

        return ResponseEntity.ok(service.getAllNews());
    }

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Find News",
            description = "Retrieve a news entry by ID or full name. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "News retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<NewsResponse> getANews(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String fullName
    ) {
        return ResponseEntity.ok(service.getANews(id, fullName));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Update News",
            description = "Update an existing news entry. File is optional."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "News updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or file",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestPart("data") @Valid NewsRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        service.updateNews(id, request, file);
        return ResponseEntity.ok("News updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Delete News",
            description = "Delete a news entry by ID. Requires authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "News removed successfully",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.deleteNews(id);
        return ResponseEntity.ok("News removed successfully");
    }
}
