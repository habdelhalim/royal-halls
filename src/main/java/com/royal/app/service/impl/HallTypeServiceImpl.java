package com.royal.app.service.impl;

import com.royal.app.service.HallTypeService;
import com.royal.app.domain.HallType;
import com.royal.app.repository.HallTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing HallType.
 */
@Service
@Transactional
public class HallTypeServiceImpl implements HallTypeService{

    private final Logger log = LoggerFactory.getLogger(HallTypeServiceImpl.class);
    
    @Inject
    private HallTypeRepository hallTypeRepository;

    /**
     * Save a hallType.
     *
     * @param hallType the entity to save
     * @return the persisted entity
     */
    public HallType save(HallType hallType) {
        log.debug("Request to save HallType : {}", hallType);
        HallType result = hallTypeRepository.save(hallType);
        return result;
    }

    /**
     *  Get all the hallTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<HallType> findAll(Pageable pageable) {
        log.debug("Request to get all HallTypes");
        Page<HallType> result = hallTypeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one hallType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public HallType findOne(Long id) {
        log.debug("Request to get HallType : {}", id);
        HallType hallType = hallTypeRepository.findOne(id);
        return hallType;
    }

    /**
     *  Delete the  hallType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HallType : {}", id);
        hallTypeRepository.delete(id);
    }
}
