package com.royal.app.service.impl;

import com.google.api.services.calendar.model.CalendarList;
import com.royal.app.domain.Event;
import com.royal.app.domain.EventExtraOption;
import com.royal.app.repository.EventRepository;
import com.royal.app.service.CustomerService;
import com.royal.app.service.EventService;
import com.royal.app.service.GoogleCalendarService;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Event.
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    @Inject
    private EventRepository eventRepository;

    @Inject
    private CustomerService customerService;

    @Inject
    private GoogleCalendarService googleCalendarService;

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

        checkTimeSlotAvailability(event);
        calculateEventPrices(event);
        saveBeneficiaries(event);

        Event result = eventRepository.save(event);
        return result;
    }

    private void saveBeneficiaries(Event event) {
        if (event.getFirstBeneficiary() != null) {
            customerService.save(event.getFirstBeneficiary());
        }

        if (event.getSecondBeneficiary() != null) {
            customerService.save(event.getSecondBeneficiary());
        }
    }

    private void calculateEventPrices(Event event) {
        double basePrice = event.getHall() != null ? event.getHall().getPrice() : 0;
        event.setBasePrice(basePrice);

        Set<EventExtraOption> options = event.getOptions();
        for (EventExtraOption option : options) {
            evaluateExtraOptionPrice(option);
        }
    }

    private void evaluateExtraOptionPrice(EventExtraOption eventExtraOption) {
        Double optionPrice = eventExtraOption.getOption().getPrice();
        Double eventExtraOptionPrice = eventExtraOption.getVariant() != null ? eventExtraOption.getVariant().getPrice() : optionPrice;
        eventExtraOption.setPrice(eventExtraOptionPrice);
    }

    private void checkTimeSlotAvailability(Event event) {
        //add margin of one hour before and after the event
        ZonedDateTime startTime = event.getEventStartDate().minus(59, ChronoUnit.MINUTES);
        ZonedDateTime endTime = event.getEventEndDate().plus(59, ChronoUnit.MINUTES);

        List<Event> events = eventRepository.findEventsInInterval(startTime, endTime, event.getHall());
        long otherEvents = events.stream().filter(event1 -> !event1.getId().equals(event.getId())).count();
        if (otherEvents > 0) {
            throw new CustomParameterizedException("event.data-conflict");
        }
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

        tryGoogleCalendar();

        Page<Event> result = eventRepository.findAll(pageable);
        return result;
    }

    private void tryGoogleCalendar() {
        try {
            CalendarList feed = googleCalendarService.getCalendarList();
            System.out.println(feed);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
