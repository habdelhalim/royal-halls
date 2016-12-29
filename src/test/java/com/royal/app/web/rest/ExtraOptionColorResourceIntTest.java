package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.ExtraOptionColor;
import com.royal.app.repository.ExtraOptionColorRepository;
import com.royal.app.service.ExtraOptionColorService;

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
 * Test class for the ExtraOptionColorResource REST controller.
 *
 * @see ExtraOptionColorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class ExtraOptionColorResourceIntTest {

    private static final String DEFAULT_COLOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COLOR_NAME = "BBBBBBBBBB";

    @Inject
    private ExtraOptionColorRepository extraOptionColorRepository;

    @Inject
    private ExtraOptionColorService extraOptionColorService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExtraOptionColorMockMvc;

    private ExtraOptionColor extraOptionColor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtraOptionColorResource extraOptionColorResource = new ExtraOptionColorResource();
        ReflectionTestUtils.setField(extraOptionColorResource, "extraOptionColorService", extraOptionColorService);
        this.restExtraOptionColorMockMvc = MockMvcBuilders.standaloneSetup(extraOptionColorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraOptionColor createEntity(EntityManager em) {
        ExtraOptionColor extraOptionColor = new ExtraOptionColor()
                .colorName(DEFAULT_COLOR_NAME);
        return extraOptionColor;
    }

    @Before
    public void initTest() {
        extraOptionColor = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtraOptionColor() throws Exception {
        int databaseSizeBeforeCreate = extraOptionColorRepository.findAll().size();

        // Create the ExtraOptionColor

        restExtraOptionColorMockMvc.perform(post("/api/extra-option-colors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOptionColor)))
            .andExpect(status().isCreated());

        // Validate the ExtraOptionColor in the database
        List<ExtraOptionColor> extraOptionColorList = extraOptionColorRepository.findAll();
        assertThat(extraOptionColorList).hasSize(databaseSizeBeforeCreate + 1);
        ExtraOptionColor testExtraOptionColor = extraOptionColorList.get(extraOptionColorList.size() - 1);
        assertThat(testExtraOptionColor.getColorName()).isEqualTo(DEFAULT_COLOR_NAME);
    }

    @Test
    @Transactional
    public void createExtraOptionColorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extraOptionColorRepository.findAll().size();

        // Create the ExtraOptionColor with an existing ID
        ExtraOptionColor existingExtraOptionColor = new ExtraOptionColor();
        existingExtraOptionColor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraOptionColorMockMvc.perform(post("/api/extra-option-colors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingExtraOptionColor)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ExtraOptionColor> extraOptionColorList = extraOptionColorRepository.findAll();
        assertThat(extraOptionColorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkColorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = extraOptionColorRepository.findAll().size();
        // set the field null
        extraOptionColor.setColorName(null);

        // Create the ExtraOptionColor, which fails.

        restExtraOptionColorMockMvc.perform(post("/api/extra-option-colors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOptionColor)))
            .andExpect(status().isBadRequest());

        List<ExtraOptionColor> extraOptionColorList = extraOptionColorRepository.findAll();
        assertThat(extraOptionColorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtraOptionColors() throws Exception {
        // Initialize the database
        extraOptionColorRepository.saveAndFlush(extraOptionColor);

        // Get all the extraOptionColorList
        restExtraOptionColorMockMvc.perform(get("/api/extra-option-colors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraOptionColor.getId().intValue())))
            .andExpect(jsonPath("$.[*].colorName").value(hasItem(DEFAULT_COLOR_NAME.toString())));
    }

    @Test
    @Transactional
    public void getExtraOptionColor() throws Exception {
        // Initialize the database
        extraOptionColorRepository.saveAndFlush(extraOptionColor);

        // Get the extraOptionColor
        restExtraOptionColorMockMvc.perform(get("/api/extra-option-colors/{id}", extraOptionColor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extraOptionColor.getId().intValue()))
            .andExpect(jsonPath("$.colorName").value(DEFAULT_COLOR_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExtraOptionColor() throws Exception {
        // Get the extraOptionColor
        restExtraOptionColorMockMvc.perform(get("/api/extra-option-colors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtraOptionColor() throws Exception {
        // Initialize the database
        extraOptionColorService.save(extraOptionColor);

        int databaseSizeBeforeUpdate = extraOptionColorRepository.findAll().size();

        // Update the extraOptionColor
        ExtraOptionColor updatedExtraOptionColor = extraOptionColorRepository.findOne(extraOptionColor.getId());
        updatedExtraOptionColor
                .colorName(UPDATED_COLOR_NAME);

        restExtraOptionColorMockMvc.perform(put("/api/extra-option-colors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtraOptionColor)))
            .andExpect(status().isOk());

        // Validate the ExtraOptionColor in the database
        List<ExtraOptionColor> extraOptionColorList = extraOptionColorRepository.findAll();
        assertThat(extraOptionColorList).hasSize(databaseSizeBeforeUpdate);
        ExtraOptionColor testExtraOptionColor = extraOptionColorList.get(extraOptionColorList.size() - 1);
        assertThat(testExtraOptionColor.getColorName()).isEqualTo(UPDATED_COLOR_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingExtraOptionColor() throws Exception {
        int databaseSizeBeforeUpdate = extraOptionColorRepository.findAll().size();

        // Create the ExtraOptionColor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExtraOptionColorMockMvc.perform(put("/api/extra-option-colors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOptionColor)))
            .andExpect(status().isCreated());

        // Validate the ExtraOptionColor in the database
        List<ExtraOptionColor> extraOptionColorList = extraOptionColorRepository.findAll();
        assertThat(extraOptionColorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExtraOptionColor() throws Exception {
        // Initialize the database
        extraOptionColorService.save(extraOptionColor);

        int databaseSizeBeforeDelete = extraOptionColorRepository.findAll().size();

        // Get the extraOptionColor
        restExtraOptionColorMockMvc.perform(delete("/api/extra-option-colors/{id}", extraOptionColor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExtraOptionColor> extraOptionColorList = extraOptionColorRepository.findAll();
        assertThat(extraOptionColorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
