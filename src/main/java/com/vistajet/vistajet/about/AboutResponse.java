package com.vistajet.vistajet.about;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AboutResponse {

    private Integer id;
    private String overview;
    private String mission;
    private String vision;
    private List<String> corePillars;
}
