package com.vistajet.vistajet.testimonials;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TestimonialsResponse {

    private Integer id;
    private String name;
    private String role;
    private String message;
    private String image;
    private LocalDateTime createdAt;
}
