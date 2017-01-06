package com.royal.app.service.impl;

import com.royal.app.domain.Contract;
import com.royal.app.repository.ContractRepository;
import com.royal.app.service.ContractService;
import com.royal.app.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing Contract.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private CustomerService customerService;

    /**
     * Save a contract.
     *
     * @param contract the entity to save
     * @return the persisted entity
     */
    public Contract save(Contract contract) {
        log.debug("Request to save Contract : {}", contract);
        if (contract.getCustomer() != null) {
            customerService.save(contract.getCustomer());
        }

        Contract result = contractRepository.save(contract);
        return result;
    }

    /**
     *  Get all the contracts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Contract> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        Page<Contract> result = contractRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get all the contracts with contact Name.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Contract> search(Pageable pageable, String searchName) {
        log.debug("Request to get all Contracts");
        long searchId = 0;
        try {
            searchId = Long.parseLong(searchName);
        } catch (NumberFormatException e) {
        }
        Page<Contract> result = contractRepository.findByCustomerCustomerNameContainsOrId(pageable, searchName, searchId);
        return result;
    }

    /**
     *  Get one contract by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Contract findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        Contract contract = contractRepository.findOne(id);
        contract.getPayments().size();
        contract.getEvents().size();
        return contract;
    }

    /**
     *  Delete the  contract by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.delete(id);
    }
}
