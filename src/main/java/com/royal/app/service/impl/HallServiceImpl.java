package com.royal.app.service.impl;

import com.royal.app.service.HallService;
import com.royal.app.domain.Hall;
import com.royal.app.repository.HallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Hall.
 */
@Service
@Transactional
public class HallServiceImpl implements HallService{

    private final Logger log = LoggerFactory.getLogger(HallServiceImpl.class);
    
    @Inject
    private HallRepository hallRepository;

    /**
     * Save a hall.
     *
     * @param hall the entity to save
     * @return the persisted entity
     */
    public Hall save(Hall hall) {
        log.debug("Request to save Hall : {}", hall);
        Hall result = hallRepository.save(hall);
        return result;
    }

    /**
     *  Get all the halls.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Hall> findAll(Pageable pageable) {
        log.debug("Request to get all Halls");
        Page<Hall> result = hallRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one hall by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Hall findOne(Long id) {
        log.debug("Request to get Hall : {}", id);
        Hall hall = hallRepository.findOne(id);
        return hall;
    }

    /**
     *  Delete the  hall by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Hall : {}", id);
        hallRepository.delete(id);
    }
}
