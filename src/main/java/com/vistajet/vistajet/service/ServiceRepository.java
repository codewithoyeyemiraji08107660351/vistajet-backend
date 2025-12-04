package com.vistajet.vistajet.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<VistajetService, Integer> {

    Optional<VistajetService> findByTitleIgnoreCase(String title);
}
