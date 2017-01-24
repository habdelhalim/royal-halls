package com.royal.app.repository;

import com.royal.app.domain.Event;
import com.royal.app.domain.Hall;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(attributePaths = {"options"})
    List<Event> findDistinctByContractId(Long contractId);

    List<Event> findEventsInInterval(@Param("start") ZonedDateTime start, @Param("end") ZonedDateTime end, @Param("hall") Hall hall);
}
