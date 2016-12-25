package com.royal.app.service;

import com.royal.app.domain.HallType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing HallType.
 */
public interface HallTypeService {

    /**
     * Save a hallType.
     *
     * @param hallType the entity to save
     * @return the persisted entity
     */
    HallType save(HallType hallType);

    /**
     *  Get all the hallTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<HallType> findAll(Pageable pageable);

    /**
     *  Get the "id" hallType.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    HallType findOne(Long id);

    /**
     *  Delete the "id" hallType.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
