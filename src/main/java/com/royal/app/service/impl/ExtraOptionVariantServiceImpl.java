package com.royal.app.service.impl;

import com.royal.app.service.ExtraOptionVariantService;
import com.royal.app.domain.ExtraOptionVariant;
import com.royal.app.repository.ExtraOptionVariantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ExtraOptionVariant.
 */
@Service
@Transactional
public class ExtraOptionVariantServiceImpl implements ExtraOptionVariantService{

    private final Logger log = LoggerFactory.getLogger(ExtraOptionVariantServiceImpl.class);
    
    @Inject
    private ExtraOptionVariantRepository extraOptionVariantRepository;

    /**
     * Save a extraOptionVariant.
     *
     * @param extraOptionVariant the entity to save
     * @return the persisted entity
     */
    public ExtraOptionVariant save(ExtraOptionVariant extraOptionVariant) {
        log.debug("Request to save ExtraOptionVariant : {}", extraOptionVariant);
        ExtraOptionVariant result = extraOptionVariantRepository.save(extraOptionVariant);
        return result;
    }

    /**
     *  Get all the extraOptionVariants.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ExtraOptionVariant> findAll(Pageable pageable) {
        log.debug("Request to get all ExtraOptionVariants");
        Page<ExtraOptionVariant> result = extraOptionVariantRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one extraOptionVariant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ExtraOptionVariant findOne(Long id) {
        log.debug("Request to get ExtraOptionVariant : {}", id);
        ExtraOptionVariant extraOptionVariant = extraOptionVariantRepository.findOne(id);
        return extraOptionVariant;
    }

    /**
     *  Delete the  extraOptionVariant by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtraOptionVariant : {}", id);
        extraOptionVariantRepository.delete(id);
    }
}
