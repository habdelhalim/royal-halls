package com.royal.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.royal.app.domain.HallType;
import com.royal.app.service.HallTypeService;
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
 * REST controller for managing HallType.
 */
@RestController
@RequestMapping("/api")
public class HallTypeResource {

    private final Logger log = LoggerFactory.getLogger(HallTypeResource.class);
        
    @Inject
    private HallTypeService hallTypeService;

    /**
     * POST  /hall-types : Create a new hallType.
     *
     * @param hallType the hallType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hallType, or with status 400 (Bad Request) if the hallType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hall-types")
    @Timed
    public ResponseEntity<HallType> createHallType(@RequestBody HallType hallType) throws URISyntaxException {
        log.debug("REST request to save HallType : {}", hallType);
        if (hallType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hallType", "idexists", "A new hallType cannot already have an ID")).body(null);
        }
        HallType result = hallTypeService.save(hallType);
        return ResponseEntity.created(new URI("/api/hall-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hallType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hall-types : Updates an existing hallType.
     *
     * @param hallType the hallType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hallType,
     * or with status 400 (Bad Request) if the hallType is not valid,
     * or with status 500 (Internal Server Error) if the hallType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hall-types")
    @Timed
    public ResponseEntity<HallType> updateHallType(@RequestBody HallType hallType) throws URISyntaxException {
        log.debug("REST request to update HallType : {}", hallType);
        if (hallType.getId() == null) {
            return createHallType(hallType);
        }
        HallType result = hallTypeService.save(hallType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hallType", hallType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hall-types : get all the hallTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hallTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/hall-types")
    @Timed
    public ResponseEntity<List<HallType>> getAllHallTypes(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of HallTypes");
        Page<HallType> page = hallTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hall-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hall-types/:id : get the "id" hallType.
     *
     * @param id the id of the hallType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hallType, or with status 404 (Not Found)
     */
    @GetMapping("/hall-types/{id}")
    @Timed
    public ResponseEntity<HallType> getHallType(@PathVariable Long id) {
        log.debug("REST request to get HallType : {}", id);
        HallType hallType = hallTypeService.findOne(id);
        return Optional.ofNullable(hallType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hall-types/:id : delete the "id" hallType.
     *
     * @param id the id of the hallType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hall-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteHallType(@PathVariable Long id) {
        log.debug("REST request to delete HallType : {}", id);
        hallTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hallType", id.toString())).build();
    }

}
