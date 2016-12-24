package com.royal.app.repository;

import com.royal.app.domain.Hall;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hall entity.
 */
@SuppressWarnings("unused")
public interface HallRepository extends JpaRepository<Hall,Long> {

}
