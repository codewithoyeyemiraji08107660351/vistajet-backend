package com.vistajet.vistajet.leadership;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadershipRepository extends JpaRepository<Leadership, Integer> {

    Optional<Leadership> findByFullNameIgnoreCase(String fullName);

}
