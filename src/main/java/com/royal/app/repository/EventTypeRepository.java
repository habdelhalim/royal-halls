package com.royal.app.repository;

import com.royal.app.domain.EventType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EventType entity.
 */
@SuppressWarnings("unused")
public interface EventTypeRepository extends JpaRepository<EventType,Long> {

}
