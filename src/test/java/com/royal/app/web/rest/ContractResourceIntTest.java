package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.Contract;
import com.royal.app.repository.ContractRepository;
import com.royal.app.service.ContractService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.royal.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.royal.app.domain.enumeration.ContractType;
import com.royal.app.domain.enumeration.ContractStatus;
/**
 * Test class for the ContractResource REST controller.
 *
 * @see ContractResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class ContractResourceIntTest {

    private static final String DEFAULT_CONTRACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NAME = "BBBBBBBBBB";

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.WEDDING;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.ENGAGEMENT;

    private static final ZonedDateTime DEFAULT_CONTRACT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CONTRACT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ContractStatus DEFAULT_CONTRACT_STATUS = ContractStatus.CREATED;
    private static final ContractStatus UPDATED_CONTRACT_STATUS = ContractStatus.APPROVED;

    private static final String DEFAULT_CONTRACT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NOTES = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT = 2D;

    private static final Double DEFAULT_OPEN_AMOUNT = 1D;
    private static final Double UPDATED_OPEN_AMOUNT = 2D;

    @Inject
    private ContractRepository contractRepository;

    @Inject
    private ContractService contractService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContractMockMvc;

    private Contract contract;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContractResource contractResource = new ContractResource();
        ReflectionTestUtils.setField(contractResource, "contractService", contractService);
        this.restContractMockMvc = MockMvcBuilders.standaloneSetup(contractResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
                .contractName(DEFAULT_CONTRACT_NAME)
                .contractType(DEFAULT_CONTRACT_TYPE)
                .contractDate(DEFAULT_CONTRACT_DATE)
                .contractStatus(DEFAULT_CONTRACT_STATUS)
                .contractNotes(DEFAULT_CONTRACT_NOTES)
                .totalAmount(DEFAULT_TOTAL_AMOUNT)
                .openAmount(DEFAULT_OPEN_AMOUNT);
        return contract;
    }

    @Before
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    public void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getContractName()).isEqualTo(DEFAULT_CONTRACT_NAME);
        assertThat(testContract.getContractType()).isEqualTo(DEFAULT_CONTRACT_TYPE);
        assertThat(testContract.getContractDate()).isEqualTo(DEFAULT_CONTRACT_DATE);
        assertThat(testContract.getContractStatus()).isEqualTo(DEFAULT_CONTRACT_STATUS);
        assertThat(testContract.getContractNotes()).isEqualTo(DEFAULT_CONTRACT_NOTES);
        assertThat(testContract.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testContract.getOpenAmount()).isEqualTo(DEFAULT_OPEN_AMOUNT);
    }

    @Test
    @Transactional
    public void createContractWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // Create the Contract with an existing ID
        Contract existingContract = new Contract();
        existingContract.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingContract)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkContractTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setContractType(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContractDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setContractDate(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContractStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractRepository.findAll().size();
        // set the field null
        contract.setContractStatus(null);

        // Create the Contract, which fails.

        restContractMockMvc.perform(post("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isBadRequest());

        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc.perform(get("/api/contracts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractName").value(hasItem(DEFAULT_CONTRACT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].contractDate").value(hasItem(sameInstant(DEFAULT_CONTRACT_DATE))))
            .andExpect(jsonPath("$.[*].contractStatus").value(hasItem(DEFAULT_CONTRACT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].contractNotes").value(hasItem(DEFAULT_CONTRACT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].openAmount").value(hasItem(DEFAULT_OPEN_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.contractName").value(DEFAULT_CONTRACT_NAME.toString()))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE.toString()))
            .andExpect(jsonPath("$.contractDate").value(sameInstant(DEFAULT_CONTRACT_DATE)))
            .andExpect(jsonPath("$.contractStatus").value(DEFAULT_CONTRACT_STATUS.toString()))
            .andExpect(jsonPath("$.contractNotes").value(DEFAULT_CONTRACT_NOTES.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.openAmount").value(DEFAULT_OPEN_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get("/api/contracts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContract() throws Exception {
        // Initialize the database
        contractService.save(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findOne(contract.getId());
        updatedContract
                .contractName(UPDATED_CONTRACT_NAME)
                .contractType(UPDATED_CONTRACT_TYPE)
                .contractDate(UPDATED_CONTRACT_DATE)
                .contractStatus(UPDATED_CONTRACT_STATUS)
                .contractNotes(UPDATED_CONTRACT_NOTES)
                .totalAmount(UPDATED_TOTAL_AMOUNT)
                .openAmount(UPDATED_OPEN_AMOUNT);

        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContract)))
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getContractName()).isEqualTo(UPDATED_CONTRACT_NAME);
        assertThat(testContract.getContractType()).isEqualTo(UPDATED_CONTRACT_TYPE);
        assertThat(testContract.getContractDate()).isEqualTo(UPDATED_CONTRACT_DATE);
        assertThat(testContract.getContractStatus()).isEqualTo(UPDATED_CONTRACT_STATUS);
        assertThat(testContract.getContractNotes()).isEqualTo(UPDATED_CONTRACT_NOTES);
        assertThat(testContract.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testContract.getOpenAmount()).isEqualTo(UPDATED_OPEN_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Create the Contract

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContractMockMvc.perform(put("/api/contracts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contract)))
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContract() throws Exception {
        // Initialize the database
        contractService.save(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Get the contract
        restContractMockMvc.perform(delete("/api/contracts/{id}", contract.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
