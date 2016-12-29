package com.royal.app.repository;

import com.royal.app.domain.EventExtraOption;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the EventExtraOption entity.
 */
@SuppressWarnings("unused")
public interface EventExtraOptionRepository extends JpaRepository<EventExtraOption,Long> {

}
