package com.royal.app.service;

import com.royal.app.domain.EventExtraOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing EventExtraOption.
 */
public interface EventExtraOptionService {

    /**
     * Save a eventExtraOption.
     *
     * @param eventExtraOption the entity to save
     * @return the persisted entity
     */
    EventExtraOption save(EventExtraOption eventExtraOption);

    /**
     *  Get all the eventExtraOptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EventExtraOption> findAll(Pageable pageable);

    /**
     *  Get the "id" eventExtraOption.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EventExtraOption findOne(Long id);

    /**
     *  Delete the "id" eventExtraOption.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
