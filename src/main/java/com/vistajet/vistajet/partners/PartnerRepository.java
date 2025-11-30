package com.vistajet.vistajet.partners;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partners, Integer> {

    Optional<Partners> findByCompanyNameIgnoreCase(String companyName);
}
