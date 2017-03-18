package com.royal.app.service.impl;

import com.royal.app.service.CustomerStatusService;
import com.royal.app.domain.CustomerStatus;
import com.royal.app.repository.CustomerStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing CustomerStatus.
 */
@Service
@Transactional
public class CustomerStatusServiceImpl implements CustomerStatusService{

    private final Logger log = LoggerFactory.getLogger(CustomerStatusServiceImpl.class);
    
    @Inject
    private CustomerStatusRepository customerStatusRepository;

    /**
     * Save a customerStatus.
     *
     * @param customerStatus the entity to save
     * @return the persisted entity
     */
    public CustomerStatus save(CustomerStatus customerStatus) {
        log.debug("Request to save CustomerStatus : {}", customerStatus);
        CustomerStatus result = customerStatusRepository.save(customerStatus);
        return result;
    }

    /**
     *  Get all the customerStatuses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CustomerStatus> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerStatuses");
        Page<CustomerStatus> result = customerStatusRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one customerStatus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CustomerStatus findOne(Long id) {
        log.debug("Request to get CustomerStatus : {}", id);
        CustomerStatus customerStatus = customerStatusRepository.findOne(id);
        return customerStatus;
    }

    /**
     *  Delete the  customerStatus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerStatus : {}", id);
        customerStatusRepository.delete(id);
    }
}
