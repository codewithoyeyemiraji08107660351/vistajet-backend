package com.vistajet.vistajet.partners;

import com.vistajet.vistajet.common.PageResponse;
import com.vistajet.vistajet.news.NewsRequest;
import com.vistajet.vistajet.news.NewsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/partners")
@CrossOrigin(origins = "*")
@Validated
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping(value = "/create-partner", consumes = "multipart/form-data")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPartner(
            @RequestPart("data") @Valid PartnerRequest request,
            @RequestPart("file") MultipartFile file) {

        partnerService.createPartner(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Partner created successfully");
    }

    @GetMapping("/all-partners")
    public ResponseEntity<PageResponse<PartnerResponse>> getAllPartners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(partnerService.getAllPartners(page, size));
    }

    @GetMapping("/find")
    public ResponseEntity<PartnerResponse> getAPartner(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String companyName
    ) {
        return ResponseEntity.ok(partnerService.getAPartner(id, companyName));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePartners(@PathVariable Integer id) {
        partnerService.deletePartners(id);
        return ResponseEntity.ok("Partner removed successfully");
    }

}
