package com.royal.app.repository;

import com.royal.app.domain.ExtraOptionColor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExtraOptionColor entity.
 */
@SuppressWarnings("unused")
public interface ExtraOptionColorRepository extends JpaRepository<ExtraOptionColor,Long> {

}
