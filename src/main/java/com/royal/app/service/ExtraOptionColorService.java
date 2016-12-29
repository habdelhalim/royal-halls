package com.royal.app.service;

import com.royal.app.domain.ExtraOptionColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing ExtraOptionColor.
 */
public interface ExtraOptionColorService {

    /**
     * Save a extraOptionColor.
     *
     * @param extraOptionColor the entity to save
     * @return the persisted entity
     */
    ExtraOptionColor save(ExtraOptionColor extraOptionColor);

    /**
     *  Get all the extraOptionColors.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExtraOptionColor> findAll(Pageable pageable);

    /**
     *  Get the "id" extraOptionColor.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExtraOptionColor findOne(Long id);

    /**
     *  Delete the "id" extraOptionColor.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
