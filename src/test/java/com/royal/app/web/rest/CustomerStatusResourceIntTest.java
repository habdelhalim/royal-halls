package com.royal.app.web.rest;

import com.royal.app.RoyalhallsApp;

import com.royal.app.domain.CustomerStatus;
import com.royal.app.repository.CustomerStatusRepository;
import com.royal.app.service.CustomerStatusService;

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
 * Test class for the CustomerStatusResource REST controller.
 *
 * @see CustomerStatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoyalhallsApp.class)
public class CustomerStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COLOUR = "AAAAAAAAAA";
    private static final String UPDATED_COLOUR = "BBBBBBBBBB";

    @Inject
    private CustomerStatusRepository customerStatusRepository;

    @Inject
    private CustomerStatusService customerStatusService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCustomerStatusMockMvc;

    private CustomerStatus customerStatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerStatusResource customerStatusResource = new CustomerStatusResource();
        ReflectionTestUtils.setField(customerStatusResource, "customerStatusService", customerStatusService);
        this.restCustomerStatusMockMvc = MockMvcBuilders.standaloneSetup(customerStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerStatus createEntity(EntityManager em) {
        CustomerStatus customerStatus = new CustomerStatus()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .colour(DEFAULT_COLOUR);
        return customerStatus;
    }

    @Before
    public void initTest() {
        customerStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerStatus() throws Exception {
        int databaseSizeBeforeCreate = customerStatusRepository.findAll().size();

        // Create the CustomerStatus

        restCustomerStatusMockMvc.perform(post("/api/customer-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerStatus)))
            .andExpect(status().isCreated());

        // Validate the CustomerStatus in the database
        List<CustomerStatus> customerStatusList = customerStatusRepository.findAll();
        assertThat(customerStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerStatus testCustomerStatus = customerStatusList.get(customerStatusList.size() - 1);
        assertThat(testCustomerStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomerStatus.getColour()).isEqualTo(DEFAULT_COLOUR);
    }

    @Test
    @Transactional
    public void createCustomerStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerStatusRepository.findAll().size();

        // Create the CustomerStatus with an existing ID
        CustomerStatus existingCustomerStatus = new CustomerStatus();
        existingCustomerStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerStatusMockMvc.perform(post("/api/customer-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingCustomerStatus)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CustomerStatus> customerStatusList = customerStatusRepository.findAll();
        assertThat(customerStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomerStatuses() throws Exception {
        // Initialize the database
        customerStatusRepository.saveAndFlush(customerStatus);

        // Get all the customerStatusList
        restCustomerStatusMockMvc.perform(get("/api/customer-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].colour").value(hasItem(DEFAULT_COLOUR.toString())));
    }

    @Test
    @Transactional
    public void getCustomerStatus() throws Exception {
        // Initialize the database
        customerStatusRepository.saveAndFlush(customerStatus);

        // Get the customerStatus
        restCustomerStatusMockMvc.perform(get("/api/customer-statuses/{id}", customerStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.colour").value(DEFAULT_COLOUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerStatus() throws Exception {
        // Get the customerStatus
        restCustomerStatusMockMvc.perform(get("/api/customer-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerStatus() throws Exception {
        // Initialize the database
        customerStatusService.save(customerStatus);

        int databaseSizeBeforeUpdate = customerStatusRepository.findAll().size();

        // Update the customerStatus
        CustomerStatus updatedCustomerStatus = customerStatusRepository.findOne(customerStatus.getId());
        updatedCustomerStatus
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .colour(UPDATED_COLOUR);

        restCustomerStatusMockMvc.perform(put("/api/customer-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerStatus)))
            .andExpect(status().isOk());

        // Validate the CustomerStatus in the database
        List<CustomerStatus> customerStatusList = customerStatusRepository.findAll();
        assertThat(customerStatusList).hasSize(databaseSizeBeforeUpdate);
        CustomerStatus testCustomerStatus = customerStatusList.get(customerStatusList.size() - 1);
        assertThat(testCustomerStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomerStatus.getColour()).isEqualTo(UPDATED_COLOUR);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerStatus() throws Exception {
        int databaseSizeBeforeUpdate = customerStatusRepository.findAll().size();

        // Create the CustomerStatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerStatusMockMvc.perform(put("/api/customer-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerStatus)))
            .andExpect(status().isCreated());

        // Validate the CustomerStatus in the database
        List<CustomerStatus> customerStatusList = customerStatusRepository.findAll();
        assertThat(customerStatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomerStatus() throws Exception {
        // Initialize the database
        customerStatusService.save(customerStatus);

        int databaseSizeBeforeDelete = customerStatusRepository.findAll().size();

        // Get the customerStatus
        restCustomerStatusMockMvc.perform(delete("/api/customer-statuses/{id}", customerStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerStatus> customerStatusList = customerStatusRepository.findAll();
        assertThat(customerStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
