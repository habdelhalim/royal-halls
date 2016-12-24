package com.royal.app.repository;

import com.royal.app.domain.Event;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event,Long> {

    @Query("select distinct event from Event event left join fetch event.options")
    List<Event> findAllWithEagerRelationships();

    @Query("select event from Event event left join fetch event.options where event.id =:id")
    Event findOneWithEagerRelationships(@Param("id") Long id);

}
