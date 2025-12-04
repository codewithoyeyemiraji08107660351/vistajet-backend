package com.vistajet.vistajet.service;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class ServiceResponse {
    private Integer id;
    private String title;
    private String description;
    private String image;
    private List<String> features;
}
