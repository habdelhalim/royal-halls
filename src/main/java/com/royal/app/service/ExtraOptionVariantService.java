package com.royal.app.service;

import com.royal.app.domain.ExtraOptionVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing ExtraOptionVariant.
 */
public interface ExtraOptionVariantService {

    /**
     * Save a extraOptionVariant.
     *
     * @param extraOptionVariant the entity to save
     * @return the persisted entity
     */
    ExtraOptionVariant save(ExtraOptionVariant extraOptionVariant);

    /**
     *  Get all the extraOptionVariants.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExtraOptionVariant> findAll(Pageable pageable);

    /**
     *  Get the "id" extraOptionVariant.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExtraOptionVariant findOne(Long id);

    /**
     *  Delete the "id" extraOptionVariant.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
