package com.royal.app.service;

import com.royal.app.domain.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing EventType.
 */
public interface EventTypeService {

    /**
     * Save a eventType.
     *
     * @param eventType the entity to save
     * @return the persisted entity
     */
    EventType save(EventType eventType);

    /**
     *  Get all the eventTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EventType> findAll(Pageable pageable);

    /**
     *  Get the "id" eventType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EventType findOne(Long id);

    /**
     *  Delete the "id" eventType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
