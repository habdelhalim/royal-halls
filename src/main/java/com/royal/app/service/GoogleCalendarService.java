package com.royal.app.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.royal.app.domain.Contract;
import com.royal.app.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collections;

/**
 * Created by habdelhalim on 7/1/17.
 */
@Service
public class GoogleCalendarService {
    private static final String GOOGLE_APP_CRED = "/google-auth-client.json";

    private NetHttpTransport httpTransport;
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    @Autowired
    private ContractService contractService;

    public CalendarList getCalendarList() throws Exception {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize();
        Calendar client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName("royalhalls")
            .build();

        return client.calendarList().list().execute();
    }

    public Event saveEvent(String googleCalendarId, com.royal.app.domain.Event reservationEvent, String googleCalendarColor) throws Exception {
        if (googleCalendarId == null) {
            throw new IllegalArgumentException("calendar Id is mandatory ");
        }

        Credential credential = authorize();
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Calendar client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName("royalhalls")
            .build();

        EventDateTime startDate = new EventDateTime();
        startDate.setDateTime(new DateTime(Date.from(reservationEvent.getEventStartDate().toInstant())));

        EventDateTime endDate = new EventDateTime();
        endDate.setDateTime(new DateTime(Date.from(reservationEvent.getEventEndDate().toInstant())));

        Event event = new Event();
        if (reservationEvent.getGoogleEventId() != null) {
            event.setId(reservationEvent.getGoogleEventId());
        }

        setEventSummary(reservationEvent, event);
        setEventDescription(reservationEvent, event);
        setEventLocation(googleCalendarId, client, event);
        event.setStart(startDate);
        event.setEnd(endDate);
        event.setColorId(googleCalendarColor);

        Event googleEvent = null;
        if (reservationEvent.getGoogleEventId() != null) {
            googleEvent = client.events()
                .update(googleCalendarId, reservationEvent.getGoogleEventId(), event)
                .execute();
        } else {
            googleEvent = client.events()
                .insert(googleCalendarId, event)
                .execute();
        }

        return googleEvent;
    }

    private void setEventSummary(com.royal.app.domain.Event reservationEvent, Event event) {
        try {
            event.setSummary(reservationEvent.getEventName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventDescription(com.royal.app.domain.Event reservationEvent, Event event) {
        try {
            StringBuilder description = new StringBuilder();
            Contract contract = contractService.findOne(reservationEvent.getContract().getId());
            addCustomerLine(description, contract.getCustomer());
            addCustomerLine(description, reservationEvent.getFirstBeneficiary());
            addCustomerLine(description, reservationEvent.getSecondBeneficiary());
            description.append(reservationEvent.getHall().getHallName());
            event.setDescription(description.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCustomerLine(StringBuilder description, final Customer customer) {
        if (customer != null) {
            description.append(customer.getCustomerName());

            if (customer.getMobile() != null) {
                description.append(" : ");
                description.append(customer.getMobile());
            }

            description.append('\n');
        }
    }

    private void setEventLocation(String googleCalendarId, Calendar client, Event event) {
        try {
            event.setLocation(client.calendars().get(googleCalendarId).execute().getLocation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Credential authorize() throws Exception {
        GoogleCredential credential = GoogleCredential
            .fromStream(GoogleCalendarService.class.getResourceAsStream(GOOGLE_APP_CRED))
            .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
        return credential;
    }

    public void removeEvent(String googleCalendarId, com.royal.app.domain.Event reservationEvent) throws Exception {
        Credential credential = authorize();
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Calendar client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName("royalhalls")
            .build();

        client.events()
            .delete(googleCalendarId, reservationEvent.getGoogleEventId()).execute();
    }
}
