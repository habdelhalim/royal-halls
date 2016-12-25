package com.royal.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.royal.app.domain.ContractType;
import com.royal.app.service.ContractTypeService;
import com.royal.app.web.rest.util.HeaderUtil;
import com.royal.app.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ContractType.
 */
@RestController
@RequestMapping("/api")
public class ContractTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContractTypeResource.class);
        
    @Inject
    private ContractTypeService contractTypeService;

    /**
     * POST  /contract-types : Create a new contractType.
     *
     * @param contractType the contractType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contractType, or with status 400 (Bad Request) if the contractType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contract-types")
    @Timed
    public ResponseEntity<ContractType> createContractType(@RequestBody ContractType contractType) throws URISyntaxException {
        log.debug("REST request to save ContractType : {}", contractType);
        if (contractType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contractType", "idexists", "A new contractType cannot already have an ID")).body(null);
        }
        ContractType result = contractTypeService.save(contractType);
        return ResponseEntity.created(new URI("/api/contract-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contractType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contract-types : Updates an existing contractType.
     *
     * @param contractType the contractType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contractType,
     * or with status 400 (Bad Request) if the contractType is not valid,
     * or with status 500 (Internal Server Error) if the contractType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contract-types")
    @Timed
    public ResponseEntity<ContractType> updateContractType(@RequestBody ContractType contractType) throws URISyntaxException {
        log.debug("REST request to update ContractType : {}", contractType);
        if (contractType.getId() == null) {
            return createContractType(contractType);
        }
        ContractType result = contractTypeService.save(contractType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contractType", contractType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contract-types : get all the contractTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contractTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/contract-types")
    @Timed
    public ResponseEntity<List<ContractType>> getAllContractTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ContractTypes");
        Page<ContractType> page = contractTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contract-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contract-types/:id : get the "id" contractType.
     *
     * @param id the id of the contractType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contractType, or with status 404 (Not Found)
     */
    @GetMapping("/contract-types/{id}")
    @Timed
    public ResponseEntity<ContractType> getContractType(@PathVariable Long id) {
        log.debug("REST request to get ContractType : {}", id);
        ContractType contractType = contractTypeService.findOne(id);
        return Optional.ofNullable(contractType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contract-types/:id : delete the "id" contractType.
     *
     * @param id the id of the contractType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contract-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteContractType(@PathVariable Long id) {
        log.debug("REST request to delete ContractType : {}", id);
        contractTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contractType", id.toString())).build();
    }

}
