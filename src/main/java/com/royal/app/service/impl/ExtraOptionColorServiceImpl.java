package com.royal.app.service.impl;

import com.royal.app.service.ExtraOptionColorService;
import com.royal.app.domain.ExtraOptionColor;
import com.royal.app.repository.ExtraOptionColorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ExtraOptionColor.
 */
@Service
@Transactional
public class ExtraOptionColorServiceImpl implements ExtraOptionColorService{

    private final Logger log = LoggerFactory.getLogger(ExtraOptionColorServiceImpl.class);
    
    @Inject
    private ExtraOptionColorRepository extraOptionColorRepository;

    /**
     * Save a extraOptionColor.
     *
     * @param extraOptionColor the entity to save
     * @return the persisted entity
     */
    public ExtraOptionColor save(ExtraOptionColor extraOptionColor) {
        log.debug("Request to save ExtraOptionColor : {}", extraOptionColor);
        ExtraOptionColor result = extraOptionColorRepository.save(extraOptionColor);
        return result;
    }

    /**
     *  Get all the extraOptionColors.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ExtraOptionColor> findAll(Pageable pageable) {
        log.debug("Request to get all ExtraOptionColors");
        Page<ExtraOptionColor> result = extraOptionColorRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one extraOptionColor by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ExtraOptionColor findOne(Long id) {
        log.debug("Request to get ExtraOptionColor : {}", id);
        ExtraOptionColor extraOptionColor = extraOptionColorRepository.findOne(id);
        return extraOptionColor;
    }

    /**
     *  Delete the  extraOptionColor by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtraOptionColor : {}", id);
        extraOptionColorRepository.delete(id);
    }
}
