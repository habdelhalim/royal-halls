package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.HallType;
import com.royal.app.repository.HallTypeRepository;
import com.royal.app.service.HallTypeService;

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
 * Test class for the HallTypeResource REST controller.
 *
 * @see HallTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class HallTypeResourceIntTest {

    private static final String DEFAULT_HALL_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_HALL_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private HallTypeRepository hallTypeRepository;

    @Inject
    private HallTypeService hallTypeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHallTypeMockMvc;

    private HallType hallType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HallTypeResource hallTypeResource = new HallTypeResource();
        ReflectionTestUtils.setField(hallTypeResource, "hallTypeService", hallTypeService);
        this.restHallTypeMockMvc = MockMvcBuilders.standaloneSetup(hallTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HallType createEntity(EntityManager em) {
        HallType hallType = new HallType()
                .hallTypeName(DEFAULT_HALL_TYPE_NAME)
                .description(DEFAULT_DESCRIPTION);
        return hallType;
    }

    @Before
    public void initTest() {
        hallType = createEntity(em);
    }

    @Test
    @Transactional
    public void createHallType() throws Exception {
        int databaseSizeBeforeCreate = hallTypeRepository.findAll().size();

        // Create the HallType

        restHallTypeMockMvc.perform(post("/api/hall-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hallType)))
            .andExpect(status().isCreated());

        // Validate the HallType in the database
        List<HallType> hallTypeList = hallTypeRepository.findAll();
        assertThat(hallTypeList).hasSize(databaseSizeBeforeCreate + 1);
        HallType testHallType = hallTypeList.get(hallTypeList.size() - 1);
        assertThat(testHallType.getHallTypeName()).isEqualTo(DEFAULT_HALL_TYPE_NAME);
        assertThat(testHallType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createHallTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hallTypeRepository.findAll().size();

        // Create the HallType with an existing ID
        HallType existingHallType = new HallType();
        existingHallType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHallTypeMockMvc.perform(post("/api/hall-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingHallType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HallType> hallTypeList = hallTypeRepository.findAll();
        assertThat(hallTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllHallTypes() throws Exception {
        // Initialize the database
        hallTypeRepository.saveAndFlush(hallType);

        // Get all the hallTypeList
        restHallTypeMockMvc.perform(get("/api/hall-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hallType.getId().intValue())))
            .andExpect(jsonPath("$.[*].hallTypeName").value(hasItem(DEFAULT_HALL_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getHallType() throws Exception {
        // Initialize the database
        hallTypeRepository.saveAndFlush(hallType);

        // Get the hallType
        restHallTypeMockMvc.perform(get("/api/hall-types/{id}", hallType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hallType.getId().intValue()))
            .andExpect(jsonPath("$.hallTypeName").value(DEFAULT_HALL_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHallType() throws Exception {
        // Get the hallType
        restHallTypeMockMvc.perform(get("/api/hall-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHallType() throws Exception {
        // Initialize the database
        hallTypeService.save(hallType);

        int databaseSizeBeforeUpdate = hallTypeRepository.findAll().size();

        // Update the hallType
        HallType updatedHallType = hallTypeRepository.findOne(hallType.getId());
        updatedHallType
                .hallTypeName(UPDATED_HALL_TYPE_NAME)
                .description(UPDATED_DESCRIPTION);

        restHallTypeMockMvc.perform(put("/api/hall-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHallType)))
            .andExpect(status().isOk());

        // Validate the HallType in the database
        List<HallType> hallTypeList = hallTypeRepository.findAll();
        assertThat(hallTypeList).hasSize(databaseSizeBeforeUpdate);
        HallType testHallType = hallTypeList.get(hallTypeList.size() - 1);
        assertThat(testHallType.getHallTypeName()).isEqualTo(UPDATED_HALL_TYPE_NAME);
        assertThat(testHallType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingHallType() throws Exception {
        int databaseSizeBeforeUpdate = hallTypeRepository.findAll().size();

        // Create the HallType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHallTypeMockMvc.perform(put("/api/hall-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hallType)))
            .andExpect(status().isCreated());

        // Validate the HallType in the database
        List<HallType> hallTypeList = hallTypeRepository.findAll();
        assertThat(hallTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHallType() throws Exception {
        // Initialize the database
        hallTypeService.save(hallType);

        int databaseSizeBeforeDelete = hallTypeRepository.findAll().size();

        // Get the hallType
        restHallTypeMockMvc.perform(delete("/api/hall-types/{id}", hallType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HallType> hallTypeList = hallTypeRepository.findAll();
        assertThat(hallTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
