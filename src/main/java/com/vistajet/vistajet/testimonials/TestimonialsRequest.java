package com.vistajet.vistajet.testimonials;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestimonialsRequest {

    private Integer Id;


    @NotBlank(message = "Name cannot be blank")
    private String name;


    @NotBlank(message = "Role cannot be blank")
    private String role;


    @NotBlank(message = "Message cannot be blank")
    private String message;

    private String image;

}
