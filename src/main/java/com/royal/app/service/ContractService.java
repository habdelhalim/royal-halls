package com.royal.app.service;

import com.royal.app.domain.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Contract.
 */
public interface ContractService {

    /**
     * Save a contract.
     *
     * @param contract the entity to save
     * @return the persisted entity
     */
    Contract save(Contract contract);

    /**
     *  Get all the contracts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Contract> findAll(Pageable pageable);

    /**
     *  Get all the contracts with contact Name.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Contract> search(Pageable pageable, String searchName);

    /**
     *  Get the "id" contract.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Contract findOne(Long id);

    /**
     *  Delete the "id" contract.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
