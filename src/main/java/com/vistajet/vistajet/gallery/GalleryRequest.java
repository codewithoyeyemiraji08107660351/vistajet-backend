package com.vistajet.vistajet.gallery;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GalleryRequest {

    private Integer id;
    private String title;
    @NotBlank(message="Category cannot be empty")
    private String category;
    private String caption;
    private String description;
    private String galleryImage;
}
