package com.vistajet.vistajet.service;

import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
public class ServiceResponse {
    private Integer id;
    private String title;
    private String description;
    private String image;
    private List<String> features;
}
