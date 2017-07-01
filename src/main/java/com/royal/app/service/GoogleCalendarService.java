package com.royal.app.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import org.springframework.stereotype.Service;

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

    private Credential authorize() throws Exception {
        GoogleCredential credential = GoogleCredential
            .fromStream(GoogleCalendarService.class.getResourceAsStream(GOOGLE_APP_CRED))
            .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
        return credential;
    }
}
