package com.vistajet.vistajet.testimonials;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "testimonials_tb")
public class Testimonials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @Column(nullable = false, length = 200)
    private String role;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = true)
    private String image;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

}
