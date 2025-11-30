package com.vistajet.vistajet.gallery;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GalleryResponse {

    private Integer id;
    private String title;
    private String category;
    private String description;
    private String galleryImage;
    private LocalDateTime createdAt;
}
