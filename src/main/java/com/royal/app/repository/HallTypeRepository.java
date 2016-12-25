package com.royal.app.repository;

import com.royal.app.domain.HallType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HallType entity.
 */
@SuppressWarnings("unused")
public interface HallTypeRepository extends JpaRepository<HallType,Long> {

}
