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

        event.setSummary(reservationEvent.getEventName());
        event.setDescription(reservationEvent.getHall().getHallName());
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

    private Credential authorize() throws Exception {
        GoogleCredential credential = GoogleCredential
            .fromStream(GoogleCalendarService.class.getResourceAsStream(GOOGLE_APP_CRED))
            .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
        return credential;
    }
}
