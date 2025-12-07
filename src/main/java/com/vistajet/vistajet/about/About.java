package com.vistajet.vistajet.about;

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
@Table(name = "about_tb")
public class About {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(columnDefinition = "TEXT")
    private String mission;

    @Column(columnDefinition = "TEXT")
    private String vision;

    @Column(columnDefinition = "TEXT")
    private String compliance;

    @ElementCollection
    @CollectionTable(
            name = "about_core_pillars",
            joinColumns = @JoinColumn(name = "about_id")
    )
    @Column(name = "pillar")
    private List<String> corePillars;
}
