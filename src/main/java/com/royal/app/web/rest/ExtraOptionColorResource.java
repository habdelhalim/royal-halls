package com.royal.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.royal.app.domain.ExtraOptionColor;
import com.royal.app.service.ExtraOptionColorService;
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
 * REST controller for managing ExtraOptionColor.
 */
@RestController
@RequestMapping("/api")
public class ExtraOptionColorResource {

    private final Logger log = LoggerFactory.getLogger(ExtraOptionColorResource.class);
        
    @Inject
    private ExtraOptionColorService extraOptionColorService;

    /**
     * POST  /extra-option-colors : Create a new extraOptionColor.
     *
     * @param extraOptionColor the extraOptionColor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extraOptionColor, or with status 400 (Bad Request) if the extraOptionColor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extra-option-colors")
    @Timed
    public ResponseEntity<ExtraOptionColor> createExtraOptionColor(@Valid @RequestBody ExtraOptionColor extraOptionColor) throws URISyntaxException {
        log.debug("REST request to save ExtraOptionColor : {}", extraOptionColor);
        if (extraOptionColor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("extraOptionColor", "idexists", "A new extraOptionColor cannot already have an ID")).body(null);
        }
        ExtraOptionColor result = extraOptionColorService.save(extraOptionColor);
        return ResponseEntity.created(new URI("/api/extra-option-colors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("extraOptionColor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extra-option-colors : Updates an existing extraOptionColor.
     *
     * @param extraOptionColor the extraOptionColor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extraOptionColor,
     * or with status 400 (Bad Request) if the extraOptionColor is not valid,
     * or with status 500 (Internal Server Error) if the extraOptionColor couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/extra-option-colors")
    @Timed
    public ResponseEntity<ExtraOptionColor> updateExtraOptionColor(@Valid @RequestBody ExtraOptionColor extraOptionColor) throws URISyntaxException {
        log.debug("REST request to update ExtraOptionColor : {}", extraOptionColor);
        if (extraOptionColor.getId() == null) {
            return createExtraOptionColor(extraOptionColor);
        }
        ExtraOptionColor result = extraOptionColorService.save(extraOptionColor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("extraOptionColor", extraOptionColor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extra-option-colors : get all the extraOptionColors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of extraOptionColors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/extra-option-colors")
    @Timed
    public ResponseEntity<List<ExtraOptionColor>> getAllExtraOptionColors(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExtraOptionColors");
        Page<ExtraOptionColor> page = extraOptionColorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extra-option-colors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /extra-option-colors/:id : get the "id" extraOptionColor.
     *
     * @param id the id of the extraOptionColor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extraOptionColor, or with status 404 (Not Found)
     */
    @GetMapping("/extra-option-colors/{id}")
    @Timed
    public ResponseEntity<ExtraOptionColor> getExtraOptionColor(@PathVariable Long id) {
        log.debug("REST request to get ExtraOptionColor : {}", id);
        ExtraOptionColor extraOptionColor = extraOptionColorService.findOne(id);
        return Optional.ofNullable(extraOptionColor)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /extra-option-colors/:id : delete the "id" extraOptionColor.
     *
     * @param id the id of the extraOptionColor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/extra-option-colors/{id}")
    @Timed
    public ResponseEntity<Void> deleteExtraOptionColor(@PathVariable Long id) {
        log.debug("REST request to delete ExtraOptionColor : {}", id);
        extraOptionColorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("extraOptionColor", id.toString())).build();
    }

}
