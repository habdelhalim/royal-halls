package com.royal.app.repository;

import com.royal.app.domain.ExtraOptionVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the ExtraOptionVariant entity.
 */
@SuppressWarnings("unused")
public interface ExtraOptionVariantRepository extends JpaRepository<ExtraOptionVariant, Long> {

    List<ExtraOptionVariant> findByOptionId(Long optionId);
}
