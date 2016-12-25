package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.ContractType;
import com.royal.app.repository.ContractTypeRepository;
import com.royal.app.service.ContractTypeService;

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
 * Test class for the ContractTypeResource REST controller.
 *
 * @see ContractTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class ContractTypeResourceIntTest {

    private static final String DEFAULT_CONTRACT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Inject
    private ContractTypeRepository contractTypeRepository;

    @Inject
    private ContractTypeService contractTypeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContractTypeMockMvc;

    private ContractType contractType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContractTypeResource contractTypeResource = new ContractTypeResource();
        ReflectionTestUtils.setField(contractTypeResource, "contractTypeService", contractTypeService);
        this.restContractTypeMockMvc = MockMvcBuilders.standaloneSetup(contractTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractType createEntity(EntityManager em) {
        ContractType contractType = new ContractType()
                .contractTypeName(DEFAULT_CONTRACT_TYPE_NAME)
                .description(DEFAULT_DESCRIPTION);
        return contractType;
    }

    @Before
    public void initTest() {
        contractType = createEntity(em);
    }

    @Test
    @Transactional
    public void createContractType() throws Exception {
        int databaseSizeBeforeCreate = contractTypeRepository.findAll().size();

        // Create the ContractType

        restContractTypeMockMvc.perform(post("/api/contract-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractType)))
            .andExpect(status().isCreated());

        // Validate the ContractType in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ContractType testContractType = contractTypeList.get(contractTypeList.size() - 1);
        assertThat(testContractType.getContractTypeName()).isEqualTo(DEFAULT_CONTRACT_TYPE_NAME);
        assertThat(testContractType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createContractTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractTypeRepository.findAll().size();

        // Create the ContractType with an existing ID
        ContractType existingContractType = new ContractType();
        existingContractType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractTypeMockMvc.perform(post("/api/contract-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingContractType)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContractTypes() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get all the contractTypeList
        restContractTypeMockMvc.perform(get("/api/contract-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractType.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractTypeName").value(hasItem(DEFAULT_CONTRACT_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getContractType() throws Exception {
        // Initialize the database
        contractTypeRepository.saveAndFlush(contractType);

        // Get the contractType
        restContractTypeMockMvc.perform(get("/api/contract-types/{id}", contractType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contractType.getId().intValue()))
            .andExpect(jsonPath("$.contractTypeName").value(DEFAULT_CONTRACT_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContractType() throws Exception {
        // Get the contractType
        restContractTypeMockMvc.perform(get("/api/contract-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContractType() throws Exception {
        // Initialize the database
        contractTypeService.save(contractType);

        int databaseSizeBeforeUpdate = contractTypeRepository.findAll().size();

        // Update the contractType
        ContractType updatedContractType = contractTypeRepository.findOne(contractType.getId());
        updatedContractType
                .contractTypeName(UPDATED_CONTRACT_TYPE_NAME)
                .description(UPDATED_DESCRIPTION);

        restContractTypeMockMvc.perform(put("/api/contract-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContractType)))
            .andExpect(status().isOk());

        // Validate the ContractType in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeUpdate);
        ContractType testContractType = contractTypeList.get(contractTypeList.size() - 1);
        assertThat(testContractType.getContractTypeName()).isEqualTo(UPDATED_CONTRACT_TYPE_NAME);
        assertThat(testContractType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingContractType() throws Exception {
        int databaseSizeBeforeUpdate = contractTypeRepository.findAll().size();

        // Create the ContractType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContractTypeMockMvc.perform(put("/api/contract-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contractType)))
            .andExpect(status().isCreated());

        // Validate the ContractType in the database
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContractType() throws Exception {
        // Initialize the database
        contractTypeService.save(contractType);

        int databaseSizeBeforeDelete = contractTypeRepository.findAll().size();

        // Get the contractType
        restContractTypeMockMvc.perform(delete("/api/contract-types/{id}", contractType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ContractType> contractTypeList = contractTypeRepository.findAll();
        assertThat(contractTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
