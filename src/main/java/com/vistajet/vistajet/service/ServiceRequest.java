package com.vistajet.vistajet.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ServiceRequest {

    private Integer id;

    @NotBlank(message = "Please provide title of the service")
    private String title;

    @NotBlank(message = "Please provide description of the service")
    private String description;

    private String image;

    @NotEmpty(message = "At least One core should be provided")
    private List<String> features;
}
