package com.royal.app.service.impl;

import com.royal.app.domain.Contract;
import com.royal.app.repository.ContractRepository;
import com.royal.app.service.ContractService;
import com.royal.app.service.CustomerService;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

/**
 * Service Implementation for managing Contract.
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

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
        Page<Contract> contracts = searchUsingLucene(searchName);
        return contracts;
    }

    private Page<Contract> searchUsingLucene(String searchName) {
        if (searchName == null || searchName.length() < 1) {
            return new PageImpl<>(new ArrayList<>());
        }
        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager =
            org.hibernate.search.jpa.Search.
                getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
            fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Contract.class).get();

        // a very basic query by keywords
        org.apache.lucene.search.Query query =
            queryBuilder
                .phrase()
                .withSlop(5)
                .onField("id")
                .andField("events.eventName").boostedTo(5)
                .andField("customer.customerName").boostedTo(3)
                .andField("customer.mobile")
                .andField("customer.telephone")
                .andField("contractNotes")
                .andField("contractDate").ignoreFieldBridge()
                .andField("eventStartDate").ignoreFieldBridge()
                .sentence(searchName)
                .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
            fullTextEntityManager.createFullTextQuery(query, Contract.class);

        // execute search and return results (sorted by relevance as default)
        return new PageImpl<>(jpaQuery.getResultList());
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
