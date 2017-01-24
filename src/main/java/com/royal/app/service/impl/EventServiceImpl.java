package com.royal.app.service.impl;

import com.royal.app.domain.Event;
import com.royal.app.repository.EventRepository;
import com.royal.app.service.EventService;
import com.royal.app.web.rest.errors.CustomParameterizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    @Inject
    private EventRepository eventRepository;

    /**
     * Save a event.
     *
     * @param event the entity to save
     * @return the persisted entity
     */
    public Event save(Event event) {
        log.debug("Request to save Event : {}", event);
        Duration offsetFromNow = Duration.between(ZonedDateTime.now(), event.getEventStartDate());
        if (offsetFromNow.toMinutes() < 0) {
            throw new CustomParameterizedException("event.past-date-range");
        }

        Duration duration = Duration.between(event.getEventStartDate(), event.getEventEndDate());
        if (duration.toMinutes() < 60) {
            throw new CustomParameterizedException("event.invalid-date-range");
        }

        List<Event> events = eventRepository.findEventsInInterval(event.getEventStartDate(), event.getEventEndDate(), event.getHall());
        long otherEvents = events.stream().filter(event1 -> !event1.getId().equals(event.getId())).count();
        if (otherEvents > 0) {
            throw new CustomParameterizedException("event.data-conflict");
        }

        Event result = eventRepository.save(event);
        return result;
    }

    /**
     *  Get all the events.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Event> findAll(Pageable pageable) {
        log.debug("Request to get all Events");
        Page<Event> result = eventRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one event by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Event findOne(Long id) {
        log.debug("Request to get Event : {}", id);
        Event event = eventRepository.findOne(id);
        if (event != null) {
            event.getOptions().size();
        }
        return event;
    }

    /**
     *  Delete the  event by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Event : {}", id);
        eventRepository.delete(id);
    }

    /**
     * Find events by  contract Id
     *
     * @param contractId
     * @return
     */
    @Override public List<Event> findAllByContractId(Long contractId) {
        return eventRepository.findDistinctByContractId(contractId);
    }
}
