package com.vistajet.vistajet.gallery;


import com.vistajet.vistajet.common.PageResponse;
import com.vistajet.vistajet.exceptions.ResourceNotFoundException;
import com.vistajet.vistajet.file.GalleryLogoStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository repository;
    private final GalleryLogoStorage galleryStorage;

    public void addGallery(GalleryRequest request, MultipartFile file) {

        String galleryImage = galleryStorage.saveGalleryLogo(file);
        Gallery gallery = Gallery.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .description(request.getDescription())
                .galleryImage(galleryImage)
                .build();
        repository.save(gallery);
    }
    public PageResponse<GalleryResponse> getAllGallery(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Gallery> gallery = repository.findAll(pageable);

        List<GalleryResponse> galleryResponseList = gallery
                .stream()
                .map(this::toResponse)
                .toList();
        return new PageResponse<>(
                galleryResponseList,
                gallery.getNumber(),
                gallery.getSize(),
                gallery.getTotalElements(),
                gallery.getTotalPages(),
                gallery.isFirst(),
                gallery.isLast()
        );
    }

    public PageResponse<GalleryResponse> getAGallery(int page, int size, List<String> categories) {

        if (categories == null || categories.isEmpty()) {
            throw new ResourceNotFoundException("Category must be provided");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Gallery> galleryPage = repository.findByCategoryIn(categories, pageable);

        if (galleryPage.isEmpty()) {
            throw new ResourceNotFoundException("No gallery found for category: ");
        }

        List<GalleryResponse> responses = galleryPage
                .stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                responses,
                galleryPage.getNumber(),
                galleryPage.getSize(),
                galleryPage.getTotalElements(),
                galleryPage.getTotalPages(),
                galleryPage.isFirst(),
                galleryPage.isLast()
        );
    }



    public void updateGallery(Integer id, GalleryRequest request, MultipartFile file) {
        Gallery gallery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gallery with ID: "+id+" not found"));

        gallery.setTitle(request.getTitle());
        gallery.setCategory(request.getCategory());
        gallery.setDescription(request.getDescription());

        if(file != null && !file.isEmpty()){
            String galleryImage = galleryStorage.saveGalleryLogo(file);
            gallery.setGalleryImage(galleryImage);
        }

        repository.save(gallery);
    }

    @Transactional
    public void deleteGallery(Integer id) {
       Gallery gallery = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gallery with ID " + id + " does not exist"
                ));

        String galleryImage = gallery.getGalleryImage();

        repository.delete(gallery);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    galleryStorage.deleteGalleryLogo(galleryImage);
                } catch (Exception e) {
                    log.error("Failed to delete file '{}' after leader delete commit", galleryImage, e);
                }
            }
        });
    }

    public GalleryResponse toResponse(Gallery gallery){
        return GalleryResponse.builder()
                .id(gallery.getId())
                .title(gallery.getTitle())
                .category(gallery.getCategory())
                .description(gallery.getDescription())
                .galleryImage(galleryStorage.loadGalleryLogo(gallery.getGalleryImage()))
                .createdAt(gallery.getCreatedAt())
                .build();
    }

}
