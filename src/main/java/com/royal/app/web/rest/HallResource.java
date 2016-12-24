package com.royal.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.royal.app.domain.Hall;
import com.royal.app.service.HallService;
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
 * REST controller for managing Hall.
 */
@RestController
@RequestMapping("/api")
public class HallResource {

    private final Logger log = LoggerFactory.getLogger(HallResource.class);
        
    @Inject
    private HallService hallService;

    /**
     * POST  /halls : Create a new hall.
     *
     * @param hall the hall to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hall, or with status 400 (Bad Request) if the hall has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/halls")
    @Timed
    public ResponseEntity<Hall> createHall(@Valid @RequestBody Hall hall) throws URISyntaxException {
        log.debug("REST request to save Hall : {}", hall);
        if (hall.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hall", "idexists", "A new hall cannot already have an ID")).body(null);
        }
        Hall result = hallService.save(hall);
        return ResponseEntity.created(new URI("/api/halls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hall", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /halls : Updates an existing hall.
     *
     * @param hall the hall to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hall,
     * or with status 400 (Bad Request) if the hall is not valid,
     * or with status 500 (Internal Server Error) if the hall couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/halls")
    @Timed
    public ResponseEntity<Hall> updateHall(@Valid @RequestBody Hall hall) throws URISyntaxException {
        log.debug("REST request to update Hall : {}", hall);
        if (hall.getId() == null) {
            return createHall(hall);
        }
        Hall result = hallService.save(hall);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hall", hall.getId().toString()))
            .body(result);
    }

    /**
     * GET  /halls : get all the halls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of halls in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/halls")
    @Timed
    public ResponseEntity<List<Hall>> getAllHalls(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Halls");
        Page<Hall> page = hallService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/halls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /halls/:id : get the "id" hall.
     *
     * @param id the id of the hall to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hall, or with status 404 (Not Found)
     */
    @GetMapping("/halls/{id}")
    @Timed
    public ResponseEntity<Hall> getHall(@PathVariable Long id) {
        log.debug("REST request to get Hall : {}", id);
        Hall hall = hallService.findOne(id);
        return Optional.ofNullable(hall)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /halls/:id : delete the "id" hall.
     *
     * @param id the id of the hall to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/halls/{id}")
    @Timed
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        log.debug("REST request to delete Hall : {}", id);
        hallService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hall", id.toString())).build();
    }

}
