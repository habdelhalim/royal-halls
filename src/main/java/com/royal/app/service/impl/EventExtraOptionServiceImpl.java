package com.royal.app.service.impl;

import com.royal.app.service.EventExtraOptionService;
import com.royal.app.domain.EventExtraOption;
import com.royal.app.repository.EventExtraOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing EventExtraOption.
 */
@Service
@Transactional
public class EventExtraOptionServiceImpl implements EventExtraOptionService{

    private final Logger log = LoggerFactory.getLogger(EventExtraOptionServiceImpl.class);
    
    @Inject
    private EventExtraOptionRepository eventExtraOptionRepository;

    /**
     * Save a eventExtraOption.
     *
     * @param eventExtraOption the entity to save
     * @return the persisted entity
     */
    public EventExtraOption save(EventExtraOption eventExtraOption) {
        log.debug("Request to save EventExtraOption : {}", eventExtraOption);
        EventExtraOption result = eventExtraOptionRepository.save(eventExtraOption);
        return result;
    }

    /**
     *  Get all the eventExtraOptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<EventExtraOption> findAll(Pageable pageable) {
        log.debug("Request to get all EventExtraOptions");
        Page<EventExtraOption> result = eventExtraOptionRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one eventExtraOption by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EventExtraOption findOne(Long id) {
        log.debug("Request to get EventExtraOption : {}", id);
        EventExtraOption eventExtraOption = eventExtraOptionRepository.findOne(id);
        return eventExtraOption;
    }

    /**
     *  Delete the  eventExtraOption by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EventExtraOption : {}", id);
        eventExtraOptionRepository.delete(id);
    }
}
