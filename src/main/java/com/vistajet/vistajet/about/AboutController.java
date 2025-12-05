package com.vistajet.vistajet.about;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://localhost:5173")
@RequestMapping("/api/v1/about")
public class AboutController {

    private final AboutService service;

    @PostMapping(value = "/add-about")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addAbout(
            @RequestBody @Valid AboutRequest request) {
        service.createAbout(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("About saved successfully");
    }

    @GetMapping("/all-about")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<AboutResponse>> getAllAbout() {
        return ResponseEntity.ok(service.getAllAbout());
    }

    @PutMapping(value = "/update/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateAbout(@PathVariable("id") Integer id,
                                         @RequestBody AboutRequest request){
        service.updateAbout(id, request);
        return ResponseEntity.ok("About updated successfully");
    }


}

