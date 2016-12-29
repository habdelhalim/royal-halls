package com.royal.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.royal.app.domain.ExtraOptionVariant;
import com.royal.app.service.ExtraOptionVariantService;
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
 * REST controller for managing ExtraOptionVariant.
 */
@RestController
@RequestMapping("/api")
public class ExtraOptionVariantResource {

    private final Logger log = LoggerFactory.getLogger(ExtraOptionVariantResource.class);
        
    @Inject
    private ExtraOptionVariantService extraOptionVariantService;

    /**
     * POST  /extra-option-variants : Create a new extraOptionVariant.
     *
     * @param extraOptionVariant the extraOptionVariant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extraOptionVariant, or with status 400 (Bad Request) if the extraOptionVariant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extra-option-variants")
    @Timed
    public ResponseEntity<ExtraOptionVariant> createExtraOptionVariant(@Valid @RequestBody ExtraOptionVariant extraOptionVariant) throws URISyntaxException {
        log.debug("REST request to save ExtraOptionVariant : {}", extraOptionVariant);
        if (extraOptionVariant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("extraOptionVariant", "idexists", "A new extraOptionVariant cannot already have an ID")).body(null);
        }
        ExtraOptionVariant result = extraOptionVariantService.save(extraOptionVariant);
        return ResponseEntity.created(new URI("/api/extra-option-variants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("extraOptionVariant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extra-option-variants : Updates an existing extraOptionVariant.
     *
     * @param extraOptionVariant the extraOptionVariant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extraOptionVariant,
     * or with status 400 (Bad Request) if the extraOptionVariant is not valid,
     * or with status 500 (Internal Server Error) if the extraOptionVariant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/extra-option-variants")
    @Timed
    public ResponseEntity<ExtraOptionVariant> updateExtraOptionVariant(@Valid @RequestBody ExtraOptionVariant extraOptionVariant) throws URISyntaxException {
        log.debug("REST request to update ExtraOptionVariant : {}", extraOptionVariant);
        if (extraOptionVariant.getId() == null) {
            return createExtraOptionVariant(extraOptionVariant);
        }
        ExtraOptionVariant result = extraOptionVariantService.save(extraOptionVariant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("extraOptionVariant", extraOptionVariant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extra-option-variants : get all the extraOptionVariants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of extraOptionVariants in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/extra-option-variants")
    @Timed
    public ResponseEntity<List<ExtraOptionVariant>> getAllExtraOptionVariants(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExtraOptionVariants");
        Page<ExtraOptionVariant> page = extraOptionVariantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extra-option-variants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /extra-option-variants/:id : get the "id" extraOptionVariant.
     *
     * @param id the id of the extraOptionVariant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extraOptionVariant, or with status 404 (Not Found)
     */
    @GetMapping("/extra-option-variants/{id}")
    @Timed
    public ResponseEntity<ExtraOptionVariant> getExtraOptionVariant(@PathVariable Long id) {
        log.debug("REST request to get ExtraOptionVariant : {}", id);
        ExtraOptionVariant extraOptionVariant = extraOptionVariantService.findOne(id);
        return Optional.ofNullable(extraOptionVariant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /extra-option-variants/:id : delete the "id" extraOptionVariant.
     *
     * @param id the id of the extraOptionVariant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/extra-option-variants/{id}")
    @Timed
    public ResponseEntity<Void> deleteExtraOptionVariant(@PathVariable Long id) {
        log.debug("REST request to delete ExtraOptionVariant : {}", id);
        extraOptionVariantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("extraOptionVariant", id.toString())).build();
    }

}
