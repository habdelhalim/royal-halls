package com.royal.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.royal.app.domain.EventExtraOption;
import com.royal.app.service.EventExtraOptionService;
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
 * REST controller for managing EventExtraOption.
 */
@RestController
@RequestMapping("/api")
public class EventExtraOptionResource {

    private final Logger log = LoggerFactory.getLogger(EventExtraOptionResource.class);
        
    @Inject
    private EventExtraOptionService eventExtraOptionService;

    /**
     * POST  /event-extra-options : Create a new eventExtraOption.
     *
     * @param eventExtraOption the eventExtraOption to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eventExtraOption, or with status 400 (Bad Request) if the eventExtraOption has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/event-extra-options")
    @Timed
    public ResponseEntity<EventExtraOption> createEventExtraOption(@RequestBody EventExtraOption eventExtraOption) throws URISyntaxException {
        log.debug("REST request to save EventExtraOption : {}", eventExtraOption);
        if (eventExtraOption.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("eventExtraOption", "idexists", "A new eventExtraOption cannot already have an ID")).body(null);
        }
        EventExtraOption result = eventExtraOptionService.save(eventExtraOption);
        return ResponseEntity.created(new URI("/api/event-extra-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("eventExtraOption", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /event-extra-options : Updates an existing eventExtraOption.
     *
     * @param eventExtraOption the eventExtraOption to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eventExtraOption,
     * or with status 400 (Bad Request) if the eventExtraOption is not valid,
     * or with status 500 (Internal Server Error) if the eventExtraOption couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/event-extra-options")
    @Timed
    public ResponseEntity<EventExtraOption> updateEventExtraOption(@RequestBody EventExtraOption eventExtraOption) throws URISyntaxException {
        log.debug("REST request to update EventExtraOption : {}", eventExtraOption);
        if (eventExtraOption.getId() == null) {
            return createEventExtraOption(eventExtraOption);
        }
        EventExtraOption result = eventExtraOptionService.save(eventExtraOption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("eventExtraOption", eventExtraOption.getId().toString()))
            .body(result);
    }

    /**
     * GET  /event-extra-options : get all the eventExtraOptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eventExtraOptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/event-extra-options")
    @Timed
    public ResponseEntity<List<EventExtraOption>> getAllEventExtraOptions(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of EventExtraOptions");
        Page<EventExtraOption> page = eventExtraOptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/event-extra-options");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /event-extra-options/:id : get the "id" eventExtraOption.
     *
     * @param id the id of the eventExtraOption to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventExtraOption, or with status 404 (Not Found)
     */
    @GetMapping("/event-extra-options/{id}")
    @Timed
    public ResponseEntity<EventExtraOption> getEventExtraOption(@PathVariable Long id) {
        log.debug("REST request to get EventExtraOption : {}", id);
        EventExtraOption eventExtraOption = eventExtraOptionService.findOne(id);
        return Optional.ofNullable(eventExtraOption)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /event-extra-options/:id : delete the "id" eventExtraOption.
     *
     * @param id the id of the eventExtraOption to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/event-extra-options/{id}")
    @Timed
    public ResponseEntity<Void> deleteEventExtraOption(@PathVariable Long id) {
        log.debug("REST request to delete EventExtraOption : {}", id);
        eventExtraOptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("eventExtraOption", id.toString())).build();
    }

}
