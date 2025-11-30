package com.vistajet.vistajet.about;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    private Integer id;
    private String overview;
    private String mission;
    private String vision;
    private List<String> corePillars;
}
