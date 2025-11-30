package com.vistajet.vistajet.gallery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    Page<Gallery> findByCategoryIn(List<String> categories, Pageable pageable);

}
