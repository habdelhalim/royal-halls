package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.Hall;
import com.royal.app.repository.HallRepository;
import com.royal.app.service.HallService;

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
 * Test class for the HallResource REST controller.
 *
 * @see HallResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class HallResourceIntTest {

    private static final String DEFAULT_HALL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HALL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Inject
    private HallRepository hallRepository;

    @Inject
    private HallService hallService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHallMockMvc;

    private Hall hall;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HallResource hallResource = new HallResource();
        ReflectionTestUtils.setField(hallResource, "hallService", hallService);
        this.restHallMockMvc = MockMvcBuilders.standaloneSetup(hallResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hall createEntity(EntityManager em) {
        Hall hall = new Hall()
                .hallName(DEFAULT_HALL_NAME)
                .description(DEFAULT_DESCRIPTION)
                .price(DEFAULT_PRICE);
        return hall;
    }

    @Before
    public void initTest() {
        hall = createEntity(em);
    }

    @Test
    @Transactional
    public void createHall() throws Exception {
        int databaseSizeBeforeCreate = hallRepository.findAll().size();

        // Create the Hall

        restHallMockMvc.perform(post("/api/halls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hall)))
            .andExpect(status().isCreated());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeCreate + 1);
        Hall testHall = hallList.get(hallList.size() - 1);
        assertThat(testHall.getHallName()).isEqualTo(DEFAULT_HALL_NAME);
        assertThat(testHall.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHall.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createHallWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hallRepository.findAll().size();

        // Create the Hall with an existing ID
        Hall existingHall = new Hall();
        existingHall.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHallMockMvc.perform(post("/api/halls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHall)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkHallNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hallRepository.findAll().size();
        // set the field null
        hall.setHallName(null);

        // Create the Hall, which fails.

        restHallMockMvc.perform(post("/api/halls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hall)))
            .andExpect(status().isBadRequest());

        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHalls() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        // Get all the hallList
        restHallMockMvc.perform(get("/api/halls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hall.getId().intValue())))
            .andExpect(jsonPath("$.[*].hallName").value(hasItem(DEFAULT_HALL_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getHall() throws Exception {
        // Initialize the database
        hallRepository.saveAndFlush(hall);

        // Get the hall
        restHallMockMvc.perform(get("/api/halls/{id}", hall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hall.getId().intValue()))
            .andExpect(jsonPath("$.hallName").value(DEFAULT_HALL_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHall() throws Exception {
        // Get the hall
        restHallMockMvc.perform(get("/api/halls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHall() throws Exception {
        // Initialize the database
        hallService.save(hall);

        int databaseSizeBeforeUpdate = hallRepository.findAll().size();

        // Update the hall
        Hall updatedHall = hallRepository.findOne(hall.getId());
        updatedHall
                .hallName(UPDATED_HALL_NAME)
                .description(UPDATED_DESCRIPTION)
                .price(UPDATED_PRICE);

        restHallMockMvc.perform(put("/api/halls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHall)))
            .andExpect(status().isOk());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate);
        Hall testHall = hallList.get(hallList.size() - 1);
        assertThat(testHall.getHallName()).isEqualTo(UPDATED_HALL_NAME);
        assertThat(testHall.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHall.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingHall() throws Exception {
        int databaseSizeBeforeUpdate = hallRepository.findAll().size();

        // Create the Hall

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHallMockMvc.perform(put("/api/halls")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hall)))
            .andExpect(status().isCreated());

        // Validate the Hall in the database
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHall() throws Exception {
        // Initialize the database
        hallService.save(hall);

        int databaseSizeBeforeDelete = hallRepository.findAll().size();

        // Get the hall
        restHallMockMvc.perform(delete("/api/halls/{id}", hall.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hall> hallList = hallRepository.findAll();
        assertThat(hallList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
