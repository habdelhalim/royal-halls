package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.ExtraOptionVariant;
import com.royal.app.repository.ExtraOptionVariantRepository;
import com.royal.app.service.ExtraOptionVariantService;

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
 * Test class for the ExtraOptionVariantResource REST controller.
 *
 * @see ExtraOptionVariantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class ExtraOptionVariantResourceIntTest {

    private static final String DEFAULT_VARIANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VARIANT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Inject
    private ExtraOptionVariantRepository extraOptionVariantRepository;

    @Inject
    private ExtraOptionVariantService extraOptionVariantService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExtraOptionVariantMockMvc;

    private ExtraOptionVariant extraOptionVariant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtraOptionVariantResource extraOptionVariantResource = new ExtraOptionVariantResource();
        ReflectionTestUtils.setField(extraOptionVariantResource, "extraOptionVariantService", extraOptionVariantService);
        this.restExtraOptionVariantMockMvc = MockMvcBuilders.standaloneSetup(extraOptionVariantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraOptionVariant createEntity(EntityManager em) {
        ExtraOptionVariant extraOptionVariant = new ExtraOptionVariant()
                .variantName(DEFAULT_VARIANT_NAME)
                .price(DEFAULT_PRICE);
        return extraOptionVariant;
    }

    @Before
    public void initTest() {
        extraOptionVariant = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtraOptionVariant() throws Exception {
        int databaseSizeBeforeCreate = extraOptionVariantRepository.findAll().size();

        // Create the ExtraOptionVariant

        restExtraOptionVariantMockMvc.perform(post("/api/extra-option-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOptionVariant)))
            .andExpect(status().isCreated());

        // Validate the ExtraOptionVariant in the database
        List<ExtraOptionVariant> extraOptionVariantList = extraOptionVariantRepository.findAll();
        assertThat(extraOptionVariantList).hasSize(databaseSizeBeforeCreate + 1);
        ExtraOptionVariant testExtraOptionVariant = extraOptionVariantList.get(extraOptionVariantList.size() - 1);
        assertThat(testExtraOptionVariant.getVariantName()).isEqualTo(DEFAULT_VARIANT_NAME);
        assertThat(testExtraOptionVariant.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createExtraOptionVariantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extraOptionVariantRepository.findAll().size();

        // Create the ExtraOptionVariant with an existing ID
        ExtraOptionVariant existingExtraOptionVariant = new ExtraOptionVariant();
        existingExtraOptionVariant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraOptionVariantMockMvc.perform(post("/api/extra-option-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingExtraOptionVariant)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ExtraOptionVariant> extraOptionVariantList = extraOptionVariantRepository.findAll();
        assertThat(extraOptionVariantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkVariantNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = extraOptionVariantRepository.findAll().size();
        // set the field null
        extraOptionVariant.setVariantName(null);

        // Create the ExtraOptionVariant, which fails.

        restExtraOptionVariantMockMvc.perform(post("/api/extra-option-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOptionVariant)))
            .andExpect(status().isBadRequest());

        List<ExtraOptionVariant> extraOptionVariantList = extraOptionVariantRepository.findAll();
        assertThat(extraOptionVariantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtraOptionVariants() throws Exception {
        // Initialize the database
        extraOptionVariantRepository.saveAndFlush(extraOptionVariant);

        // Get all the extraOptionVariantList
        restExtraOptionVariantMockMvc.perform(get("/api/extra-option-variants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraOptionVariant.getId().intValue())))
            .andExpect(jsonPath("$.[*].variantName").value(hasItem(DEFAULT_VARIANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getExtraOptionVariant() throws Exception {
        // Initialize the database
        extraOptionVariantRepository.saveAndFlush(extraOptionVariant);

        // Get the extraOptionVariant
        restExtraOptionVariantMockMvc.perform(get("/api/extra-option-variants/{id}", extraOptionVariant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extraOptionVariant.getId().intValue()))
            .andExpect(jsonPath("$.variantName").value(DEFAULT_VARIANT_NAME.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExtraOptionVariant() throws Exception {
        // Get the extraOptionVariant
        restExtraOptionVariantMockMvc.perform(get("/api/extra-option-variants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtraOptionVariant() throws Exception {
        // Initialize the database
        extraOptionVariantService.save(extraOptionVariant);

        int databaseSizeBeforeUpdate = extraOptionVariantRepository.findAll().size();

        // Update the extraOptionVariant
        ExtraOptionVariant updatedExtraOptionVariant = extraOptionVariantRepository.findOne(extraOptionVariant.getId());
        updatedExtraOptionVariant
                .variantName(UPDATED_VARIANT_NAME)
                .price(UPDATED_PRICE);

        restExtraOptionVariantMockMvc.perform(put("/api/extra-option-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtraOptionVariant)))
            .andExpect(status().isOk());

        // Validate the ExtraOptionVariant in the database
        List<ExtraOptionVariant> extraOptionVariantList = extraOptionVariantRepository.findAll();
        assertThat(extraOptionVariantList).hasSize(databaseSizeBeforeUpdate);
        ExtraOptionVariant testExtraOptionVariant = extraOptionVariantList.get(extraOptionVariantList.size() - 1);
        assertThat(testExtraOptionVariant.getVariantName()).isEqualTo(UPDATED_VARIANT_NAME);
        assertThat(testExtraOptionVariant.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingExtraOptionVariant() throws Exception {
        int databaseSizeBeforeUpdate = extraOptionVariantRepository.findAll().size();

        // Create the ExtraOptionVariant

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExtraOptionVariantMockMvc.perform(put("/api/extra-option-variants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOptionVariant)))
            .andExpect(status().isCreated());

        // Validate the ExtraOptionVariant in the database
        List<ExtraOptionVariant> extraOptionVariantList = extraOptionVariantRepository.findAll();
        assertThat(extraOptionVariantList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExtraOptionVariant() throws Exception {
        // Initialize the database
        extraOptionVariantService.save(extraOptionVariant);

        int databaseSizeBeforeDelete = extraOptionVariantRepository.findAll().size();

        // Get the extraOptionVariant
        restExtraOptionVariantMockMvc.perform(delete("/api/extra-option-variants/{id}", extraOptionVariant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExtraOptionVariant> extraOptionVariantList = extraOptionVariantRepository.findAll();
        assertThat(extraOptionVariantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
