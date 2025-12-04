package com.vistajet.vistajet.news;

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
@CrossOrigin(origins = "https://customshangarservices.netlify.app/")
@Validated
public class NewsController {

    private final NewsService service;

    @PostMapping(value = "/create-news", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createNews(
            @RequestPart("data") @Valid NewsRequest request,
            @RequestPart("file") MultipartFile file) {

        service.createNews(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("News created successfully");
    }

    @GetMapping("/all-news")
    public ResponseEntity<List<NewsResponse>> getAllNews() {
        return ResponseEntity.ok(service.getAllNews());
    }

    @GetMapping("/find")
    public ResponseEntity<NewsResponse> getNews(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String fullName
    ) {
        return ResponseEntity.ok(service.getANews(id, fullName));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestPart("data") @Valid NewsRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        service.updateNews(id, request, file);
        return ResponseEntity.ok("News updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.deleteNews(id);
        return ResponseEntity.ok("News removed successfully");
    }
}
