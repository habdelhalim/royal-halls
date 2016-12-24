package com.royal.app.service;

import com.royal.app.domain.ExtraOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing ExtraOption.
 */
public interface ExtraOptionService {

    /**
     * Save a extraOption.
     *
     * @param extraOption the entity to save
     * @return the persisted entity
     */
    ExtraOption save(ExtraOption extraOption);

    /**
     *  Get all the extraOptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ExtraOption> findAll(Pageable pageable);

    /**
     *  Get the "id" extraOption.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExtraOption findOne(Long id);

    /**
     *  Delete the "id" extraOption.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
