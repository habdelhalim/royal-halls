package com.royal.app.service.impl;

import com.royal.app.service.ContractTypeService;
import com.royal.app.domain.ContractType;
import com.royal.app.repository.ContractTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ContractType.
 */
@Service
@Transactional
public class ContractTypeServiceImpl implements ContractTypeService{

    private final Logger log = LoggerFactory.getLogger(ContractTypeServiceImpl.class);
    
    @Inject
    private ContractTypeRepository contractTypeRepository;

    /**
     * Save a contractType.
     *
     * @param contractType the entity to save
     * @return the persisted entity
     */
    public ContractType save(ContractType contractType) {
        log.debug("Request to save ContractType : {}", contractType);
        ContractType result = contractTypeRepository.save(contractType);
        return result;
    }

    /**
     *  Get all the contractTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ContractType> findAll(Pageable pageable) {
        log.debug("Request to get all ContractTypes");
        Page<ContractType> result = contractTypeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one contractType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ContractType findOne(Long id) {
        log.debug("Request to get ContractType : {}", id);
        ContractType contractType = contractTypeRepository.findOne(id);
        return contractType;
    }

    /**
     *  Delete the  contractType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ContractType : {}", id);
        contractTypeRepository.delete(id);
    }
}
