package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.EventExtraOption;
import com.royal.app.repository.EventExtraOptionRepository;
import com.royal.app.service.EventExtraOptionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventExtraOptionResource REST controller.
 *
 * @see EventExtraOptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class EventExtraOptionResourceIntTest {

    private static final Double DEFAULT_OPTION_QTY = 1D;
    private static final Double UPDATED_OPTION_QTY = 2D;

    private static final String DEFAULT_OPTION_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_NOTES = "BBBBBBBBBB";

    @Inject
    private EventExtraOptionRepository eventExtraOptionRepository;

    @Inject
    private EventExtraOptionService eventExtraOptionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEventExtraOptionMockMvc;

    private EventExtraOption eventExtraOption;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventExtraOptionResource eventExtraOptionResource = new EventExtraOptionResource();
        ReflectionTestUtils.setField(eventExtraOptionResource, "eventExtraOptionService", eventExtraOptionService);
        this.restEventExtraOptionMockMvc = MockMvcBuilders.standaloneSetup(eventExtraOptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventExtraOption createEntity(EntityManager em) {
        EventExtraOption eventExtraOption = new EventExtraOption()
                .optionQty(DEFAULT_OPTION_QTY)
                .optionNotes(DEFAULT_OPTION_NOTES);
        return eventExtraOption;
    }

    @Before
    public void initTest() {
        eventExtraOption = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventExtraOption() throws Exception {
        int databaseSizeBeforeCreate = eventExtraOptionRepository.findAll().size();

        // Create the EventExtraOption

        restEventExtraOptionMockMvc.perform(post("/api/event-extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventExtraOption)))
            .andExpect(status().isCreated());

        // Validate the EventExtraOption in the database
        List<EventExtraOption> eventExtraOptionList = eventExtraOptionRepository.findAll();
        assertThat(eventExtraOptionList).hasSize(databaseSizeBeforeCreate + 1);
        EventExtraOption testEventExtraOption = eventExtraOptionList.get(eventExtraOptionList.size() - 1);
        assertThat(testEventExtraOption.getOptionQty()).isEqualTo(DEFAULT_OPTION_QTY);
        assertThat(testEventExtraOption.getOptionNotes()).isEqualTo(DEFAULT_OPTION_NOTES);
    }

    @Test
    @Transactional
    public void createEventExtraOptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventExtraOptionRepository.findAll().size();

        // Create the EventExtraOption with an existing ID
        EventExtraOption existingEventExtraOption = new EventExtraOption();
        existingEventExtraOption.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventExtraOptionMockMvc.perform(post("/api/event-extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEventExtraOption)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EventExtraOption> eventExtraOptionList = eventExtraOptionRepository.findAll();
        assertThat(eventExtraOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEventExtraOptions() throws Exception {
        // Initialize the database
        eventExtraOptionRepository.saveAndFlush(eventExtraOption);

        // Get all the eventExtraOptionList
        restEventExtraOptionMockMvc.perform(get("/api/event-extra-options?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventExtraOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].optionQty").value(hasItem(DEFAULT_OPTION_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].optionNotes").value(hasItem(DEFAULT_OPTION_NOTES.toString())));
    }

    @Test
    @Transactional
    public void getEventExtraOption() throws Exception {
        // Initialize the database
        eventExtraOptionRepository.saveAndFlush(eventExtraOption);

        // Get the eventExtraOption
        restEventExtraOptionMockMvc.perform(get("/api/event-extra-options/{id}", eventExtraOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventExtraOption.getId().intValue()))
            .andExpect(jsonPath("$.optionQty").value(DEFAULT_OPTION_QTY.doubleValue()))
            .andExpect(jsonPath("$.optionNotes").value(DEFAULT_OPTION_NOTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventExtraOption() throws Exception {
        // Get the eventExtraOption
        restEventExtraOptionMockMvc.perform(get("/api/event-extra-options/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventExtraOption() throws Exception {
        // Initialize the database
        eventExtraOptionService.save(eventExtraOption);

        int databaseSizeBeforeUpdate = eventExtraOptionRepository.findAll().size();

        // Update the eventExtraOption
        EventExtraOption updatedEventExtraOption = eventExtraOptionRepository.findOne(eventExtraOption.getId());
        updatedEventExtraOption
                .optionQty(UPDATED_OPTION_QTY)
                .optionNotes(UPDATED_OPTION_NOTES);

        restEventExtraOptionMockMvc.perform(put("/api/event-extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEventExtraOption)))
            .andExpect(status().isOk());

        // Validate the EventExtraOption in the database
        List<EventExtraOption> eventExtraOptionList = eventExtraOptionRepository.findAll();
        assertThat(eventExtraOptionList).hasSize(databaseSizeBeforeUpdate);
        EventExtraOption testEventExtraOption = eventExtraOptionList.get(eventExtraOptionList.size() - 1);
        assertThat(testEventExtraOption.getOptionQty()).isEqualTo(UPDATED_OPTION_QTY);
        assertThat(testEventExtraOption.getOptionNotes()).isEqualTo(UPDATED_OPTION_NOTES);
    }

    @Test
    @Transactional
    public void updateNonExistingEventExtraOption() throws Exception {
        int databaseSizeBeforeUpdate = eventExtraOptionRepository.findAll().size();

        // Create the EventExtraOption

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventExtraOptionMockMvc.perform(put("/api/event-extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventExtraOption)))
            .andExpect(status().isCreated());

        // Validate the EventExtraOption in the database
        List<EventExtraOption> eventExtraOptionList = eventExtraOptionRepository.findAll();
        assertThat(eventExtraOptionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEventExtraOption() throws Exception {
        // Initialize the database
        eventExtraOptionService.save(eventExtraOption);

        int databaseSizeBeforeDelete = eventExtraOptionRepository.findAll().size();

        // Get the eventExtraOption
        restEventExtraOptionMockMvc.perform(delete("/api/event-extra-options/{id}", eventExtraOption.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EventExtraOption> eventExtraOptionList = eventExtraOptionRepository.findAll();
        assertThat(eventExtraOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
