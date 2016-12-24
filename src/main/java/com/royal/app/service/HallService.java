package com.royal.app.service;

import com.royal.app.domain.Hall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Hall.
 */
public interface HallService {

    /**
     * Save a hall.
     *
     * @param hall the entity to save
     * @return the persisted entity
     */
    Hall save(Hall hall);

    /**
     *  Get all the halls.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Hall> findAll(Pageable pageable);

    /**
     *  Get the "id" hall.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Hall findOne(Long id);

    /**
     *  Delete the "id" hall.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
