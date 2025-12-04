package com.vistajet.vistajet.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "service_tb")
public class VistajetService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String image;

    @ElementCollection
    @CollectionTable(
            name = "service_features",
            joinColumns = @JoinColumn(name = "service_id")
    )
    private List<String> features;

}
