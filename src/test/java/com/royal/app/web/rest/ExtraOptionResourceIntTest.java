package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.ExtraOption;
import com.royal.app.repository.ExtraOptionRepository;
import com.royal.app.service.ExtraOptionService;

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

import com.royal.app.domain.enumeration.OptionType;
/**
 * Test class for the ExtraOptionResource REST controller.
 *
 * @see ExtraOptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class ExtraOptionResourceIntTest {

    private static final String DEFAULT_OPTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OPTION_NAME = "BBBBBBBBBB";

    private static final OptionType DEFAULT_OPTION_TYPE = OptionType.BASIC;
    private static final OptionType UPDATED_OPTION_TYPE = OptionType.OPTIONAL;

    private static final Double DEFAULT_OPTION_QTY = 1D;
    private static final Double UPDATED_OPTION_QTY = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Inject
    private ExtraOptionRepository extraOptionRepository;

    @Inject
    private ExtraOptionService extraOptionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExtraOptionMockMvc;

    private ExtraOption extraOption;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExtraOptionResource extraOptionResource = new ExtraOptionResource();
        ReflectionTestUtils.setField(extraOptionResource, "extraOptionService", extraOptionService);
        this.restExtraOptionMockMvc = MockMvcBuilders.standaloneSetup(extraOptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraOption createEntity(EntityManager em) {
        ExtraOption extraOption = new ExtraOption()
                .optionName(DEFAULT_OPTION_NAME)
                .optionType(DEFAULT_OPTION_TYPE)
                .optionQty(DEFAULT_OPTION_QTY)
                .price(DEFAULT_PRICE);
        return extraOption;
    }

    @Before
    public void initTest() {
        extraOption = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtraOption() throws Exception {
        int databaseSizeBeforeCreate = extraOptionRepository.findAll().size();

        // Create the ExtraOption

        restExtraOptionMockMvc.perform(post("/api/extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOption)))
            .andExpect(status().isCreated());

        // Validate the ExtraOption in the database
        List<ExtraOption> extraOptionList = extraOptionRepository.findAll();
        assertThat(extraOptionList).hasSize(databaseSizeBeforeCreate + 1);
        ExtraOption testExtraOption = extraOptionList.get(extraOptionList.size() - 1);
        assertThat(testExtraOption.getOptionName()).isEqualTo(DEFAULT_OPTION_NAME);
        assertThat(testExtraOption.getOptionType()).isEqualTo(DEFAULT_OPTION_TYPE);
        assertThat(testExtraOption.getOptionQty()).isEqualTo(DEFAULT_OPTION_QTY);
        assertThat(testExtraOption.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createExtraOptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extraOptionRepository.findAll().size();

        // Create the ExtraOption with an existing ID
        ExtraOption existingExtraOption = new ExtraOption();
        existingExtraOption.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraOptionMockMvc.perform(post("/api/extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingExtraOption)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ExtraOption> extraOptionList = extraOptionRepository.findAll();
        assertThat(extraOptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOptionNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = extraOptionRepository.findAll().size();
        // set the field null
        extraOption.setOptionName(null);

        // Create the ExtraOption, which fails.

        restExtraOptionMockMvc.perform(post("/api/extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOption)))
            .andExpect(status().isBadRequest());

        List<ExtraOption> extraOptionList = extraOptionRepository.findAll();
        assertThat(extraOptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExtraOptions() throws Exception {
        // Initialize the database
        extraOptionRepository.saveAndFlush(extraOption);

        // Get all the extraOptionList
        restExtraOptionMockMvc.perform(get("/api/extra-options?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraOption.getId().intValue())))
            .andExpect(jsonPath("$.[*].optionName").value(hasItem(DEFAULT_OPTION_NAME.toString())))
            .andExpect(jsonPath("$.[*].optionType").value(hasItem(DEFAULT_OPTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].optionQty").value(hasItem(DEFAULT_OPTION_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    public void getExtraOption() throws Exception {
        // Initialize the database
        extraOptionRepository.saveAndFlush(extraOption);

        // Get the extraOption
        restExtraOptionMockMvc.perform(get("/api/extra-options/{id}", extraOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extraOption.getId().intValue()))
            .andExpect(jsonPath("$.optionName").value(DEFAULT_OPTION_NAME.toString()))
            .andExpect(jsonPath("$.optionType").value(DEFAULT_OPTION_TYPE.toString()))
            .andExpect(jsonPath("$.optionQty").value(DEFAULT_OPTION_QTY.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExtraOption() throws Exception {
        // Get the extraOption
        restExtraOptionMockMvc.perform(get("/api/extra-options/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtraOption() throws Exception {
        // Initialize the database
        extraOptionService.save(extraOption);

        int databaseSizeBeforeUpdate = extraOptionRepository.findAll().size();

        // Update the extraOption
        ExtraOption updatedExtraOption = extraOptionRepository.findOne(extraOption.getId());
        updatedExtraOption
                .optionName(UPDATED_OPTION_NAME)
                .optionType(UPDATED_OPTION_TYPE)
                .optionQty(UPDATED_OPTION_QTY)
                .price(UPDATED_PRICE);

        restExtraOptionMockMvc.perform(put("/api/extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtraOption)))
            .andExpect(status().isOk());

        // Validate the ExtraOption in the database
        List<ExtraOption> extraOptionList = extraOptionRepository.findAll();
        assertThat(extraOptionList).hasSize(databaseSizeBeforeUpdate);
        ExtraOption testExtraOption = extraOptionList.get(extraOptionList.size() - 1);
        assertThat(testExtraOption.getOptionName()).isEqualTo(UPDATED_OPTION_NAME);
        assertThat(testExtraOption.getOptionType()).isEqualTo(UPDATED_OPTION_TYPE);
        assertThat(testExtraOption.getOptionQty()).isEqualTo(UPDATED_OPTION_QTY);
        assertThat(testExtraOption.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingExtraOption() throws Exception {
        int databaseSizeBeforeUpdate = extraOptionRepository.findAll().size();

        // Create the ExtraOption

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restExtraOptionMockMvc.perform(put("/api/extra-options")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extraOption)))
            .andExpect(status().isCreated());

        // Validate the ExtraOption in the database
        List<ExtraOption> extraOptionList = extraOptionRepository.findAll();
        assertThat(extraOptionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteExtraOption() throws Exception {
        // Initialize the database
        extraOptionService.save(extraOption);

        int databaseSizeBeforeDelete = extraOptionRepository.findAll().size();

        // Get the extraOption
        restExtraOptionMockMvc.perform(delete("/api/extra-options/{id}", extraOption.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExtraOption> extraOptionList = extraOptionRepository.findAll();
        assertThat(extraOptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
