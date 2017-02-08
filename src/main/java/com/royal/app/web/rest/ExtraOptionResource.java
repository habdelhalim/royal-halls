package com.royal.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.royal.app.domain.ExtraOption;
import com.royal.app.service.ExtraOptionService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ExtraOption.
 */
@RestController
@RequestMapping("/api")
public class ExtraOptionResource {

    private final Logger log = LoggerFactory.getLogger(ExtraOptionResource.class);

    @Inject
    private ExtraOptionService extraOptionService;

    /**
     * POST  /extra-options : Create a new extraOption.
     *
     * @param extraOption the extraOption to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extraOption, or with status 400 (Bad Request) if the extraOption has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extra-options")
    @Timed
    public ResponseEntity<ExtraOption> createExtraOption(@Valid @RequestBody ExtraOption extraOption) throws URISyntaxException {
        log.debug("REST request to save ExtraOption : {}", extraOption);
        if (extraOption.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("extraOption", "idexists", "A new extraOption cannot already have an ID")).body(null);
        }
        ExtraOption result = extraOptionService.save(extraOption);
        return ResponseEntity.created(new URI("/api/extra-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("extraOption", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extra-options : Updates an existing extraOption.
     *
     * @param extraOption the extraOption to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extraOption,
     * or with status 400 (Bad Request) if the extraOption is not valid,
     * or with status 500 (Internal Server Error) if the extraOption couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/extra-options")
    @Timed
    public ResponseEntity<ExtraOption> updateExtraOption(@Valid @RequestBody ExtraOption extraOption) throws URISyntaxException {
        log.debug("REST request to update ExtraOption : {}", extraOption);
        if (extraOption.getId() == null) {
            return createExtraOption(extraOption);
        }
        ExtraOption result = extraOptionService.save(extraOption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("extraOption", extraOption.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extra-options : get all the extraOptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of extraOptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/extra-options")
    @Timed
    public ResponseEntity<List<ExtraOption>> getAllExtraOptions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExtraOptions");
        Page<ExtraOption> page = extraOptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extra-options");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /extra-options : get all the extraOptions.
     *
     * @param type the option type information
     * @return the ResponseEntity with status 200 (OK) and the list of extraOptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/extra-options/by-type/{type}")
    @Timed
    public ResponseEntity<List<ExtraOption>> getAllExtraOptionsByType(@PathVariable String type)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExtraOptions by type {}", type);
        List<ExtraOption> options = extraOptionService.findAllByType(type);
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    /**
     * GET  /extra-options/:id : get the "id" extraOption.
     *
     * @param id the id of the extraOption to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extraOption, or with status 404 (Not Found)
     */
    @GetMapping("/extra-options/{id}")
    @Timed
    public ResponseEntity<ExtraOption> getExtraOption(@PathVariable Long id) {
        log.debug("REST request to get ExtraOption : {}", id);
        ExtraOption extraOption = extraOptionService.findOne(id);
        return Optional.ofNullable(extraOption)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /extra-options/:id : delete the "id" extraOption.
     *
     * @param id the id of the extraOption to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/extra-options/{id}")
    @Timed
    public ResponseEntity<Void> deleteExtraOption(@PathVariable Long id) {
        log.debug("REST request to delete ExtraOption : {}", id);
        extraOptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("extraOption", id.toString())).build();
    }

}
