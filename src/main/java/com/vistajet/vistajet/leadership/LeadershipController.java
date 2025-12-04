package com.vistajet.vistajet.leadership;

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
@CrossOrigin(origins = "https://customshangarservices.netlify.app/")
@Validated
public class LeadershipController {

    private final LeadershipService leadershipService;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> register(
            @RequestPart("data") @Valid LeadershipRequest request,
            @RequestPart("file") MultipartFile file) {

        leadershipService.register(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Leadership created successfully");
    }

    @GetMapping("/leaders")
    public ResponseEntity<List<LeadershipResponse>> getAll() {
        return ResponseEntity.ok(leadershipService.getAllLeaders());
    }

    @GetMapping("/leader/find")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LeadershipResponse> getLeader(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String fullName
    ) {
        return ResponseEntity.ok(leadershipService.getALeadership(id, fullName));
    }

    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestPart("data") @Valid LeadershipRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        leadershipService.update(id, request, file);
        return ResponseEntity.ok("Leadership updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        leadershipService.delete(id);
        return ResponseEntity.ok("Leadership removed successfully");
    }
}
