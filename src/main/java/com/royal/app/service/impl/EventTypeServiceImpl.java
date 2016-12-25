package com.royal.app.service.impl;

import com.royal.app.service.EventTypeService;
import com.royal.app.domain.EventType;
import com.royal.app.repository.EventTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing EventType.
 */
@Service
@Transactional
public class EventTypeServiceImpl implements EventTypeService{

    private final Logger log = LoggerFactory.getLogger(EventTypeServiceImpl.class);
    
    @Inject
    private EventTypeRepository eventTypeRepository;

    /**
     * Save a eventType.
     *
     * @param eventType the entity to save
     * @return the persisted entity
     */
    public EventType save(EventType eventType) {
        log.debug("Request to save EventType : {}", eventType);
        EventType result = eventTypeRepository.save(eventType);
        return result;
    }

    /**
     *  Get all the eventTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<EventType> findAll(Pageable pageable) {
        log.debug("Request to get all EventTypes");
        Page<EventType> result = eventTypeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one eventType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EventType findOne(Long id) {
        log.debug("Request to get EventType : {}", id);
        EventType eventType = eventTypeRepository.findOne(id);
        return eventType;
    }

    /**
     *  Delete the  eventType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EventType : {}", id);
        eventTypeRepository.delete(id);
    }
}
