package com.vistajet.vistajet.testimonials;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestimonialsRepository extends JpaRepository<Testimonials, Integer> {

    Optional<Testimonials> findByNameIgnoreCase(String name);
}
