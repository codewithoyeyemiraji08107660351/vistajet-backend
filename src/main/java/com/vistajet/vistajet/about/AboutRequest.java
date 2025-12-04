package com.vistajet.vistajet.about;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AboutRequest {

    private Integer id;

    @NotBlank(message = "Overview cannot be empty")
    private String overview;

    @NotBlank(message = "Mission cannot be empty")
    private String mission;

    @NotBlank(message = "Vision cannot be empty")
    private String vision;

    @NotEmpty(message = "At least One core pillar should be provided")
    private List<String> corePillars;
}
