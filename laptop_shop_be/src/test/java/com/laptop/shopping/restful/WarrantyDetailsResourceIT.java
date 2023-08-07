package com.laptop.shopping.restful;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.laptop.shopping.IntegrationTest;
import com.laptop.shopping.domain.WarrantyDetails;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WarrantyDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarrantyDetailsResourceIT {

    private static final Long DEFAULT_WARRANTY_ID = 1L;
    private static final Long UPDATED_WARRANTY_ID = 2L;

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PAY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/warranty-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private com.laptop.shopping.repository.iRepository.IWarrantyDetailsRepository IWarrantyDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarrantyDetailsMockMvc;

    private WarrantyDetails warrantyDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarrantyDetails createEntity(EntityManager em) {
        WarrantyDetails warrantyDetails = new WarrantyDetails()
            .warrantyId(DEFAULT_WARRANTY_ID)
            .productCode(DEFAULT_PRODUCT_CODE)
            .note(DEFAULT_NOTE)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .payDate(DEFAULT_PAY_DATE);
        return warrantyDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarrantyDetails createUpdatedEntity(EntityManager em) {
        WarrantyDetails warrantyDetails = new WarrantyDetails()
            .warrantyId(UPDATED_WARRANTY_ID)
            .productCode(UPDATED_PRODUCT_CODE)
            .note(UPDATED_NOTE)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .payDate(UPDATED_PAY_DATE);
        return warrantyDetails;
    }

    @BeforeEach
    public void initTest() {
        warrantyDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createWarrantyDetails() throws Exception {
        int databaseSizeBeforeCreate = IWarrantyDetailsRepository.findAll().size();
        // Create the WarrantyDetails
        restWarrantyDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isCreated());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        WarrantyDetails testWarrantyDetails = warrantyDetailsList.get(warrantyDetailsList.size() - 1);
        assertThat(testWarrantyDetails.getWarrantyId()).isEqualTo(DEFAULT_WARRANTY_ID);
        assertThat(testWarrantyDetails.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testWarrantyDetails.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testWarrantyDetails.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testWarrantyDetails.getPayDate()).isEqualTo(DEFAULT_PAY_DATE);
    }

    @Test
    @Transactional
    void createWarrantyDetailsWithExistingId() throws Exception {
        // Create the WarrantyDetails with an existing ID
        warrantyDetails.setId(1L);

        int databaseSizeBeforeCreate = IWarrantyDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarrantyDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWarrantyDetails() throws Exception {
        // Initialize the database
        IWarrantyDetailsRepository.saveAndFlush(warrantyDetails);

        // Get all the warrantyDetailsList
        restWarrantyDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warrantyDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].warrantyId").value(hasItem(DEFAULT_WARRANTY_ID.intValue())))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].payDate").value(hasItem(DEFAULT_PAY_DATE.toString())));
    }

    @Test
    @Transactional
    void getWarrantyDetails() throws Exception {
        // Initialize the database
        IWarrantyDetailsRepository.saveAndFlush(warrantyDetails);

        // Get the warrantyDetails
        restWarrantyDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, warrantyDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warrantyDetails.getId().intValue()))
            .andExpect(jsonPath("$.warrantyId").value(DEFAULT_WARRANTY_ID.intValue()))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.payDate").value(DEFAULT_PAY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWarrantyDetails() throws Exception {
        // Get the warrantyDetails
        restWarrantyDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWarrantyDetails() throws Exception {
        // Initialize the database
        IWarrantyDetailsRepository.saveAndFlush(warrantyDetails);

        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();

        // Update the warrantyDetails
        WarrantyDetails updatedWarrantyDetails = IWarrantyDetailsRepository.findById(warrantyDetails.getId()).get();
        // Disconnect from session so that the updates on updatedWarrantyDetails are not directly saved in db
        em.detach(updatedWarrantyDetails);
        updatedWarrantyDetails
            .warrantyId(UPDATED_WARRANTY_ID)
            .productCode(UPDATED_PRODUCT_CODE)
            .note(UPDATED_NOTE)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .payDate(UPDATED_PAY_DATE);

        restWarrantyDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWarrantyDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWarrantyDetails))
            )
            .andExpect(status().isOk());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
        WarrantyDetails testWarrantyDetails = warrantyDetailsList.get(warrantyDetailsList.size() - 1);
        assertThat(testWarrantyDetails.getWarrantyId()).isEqualTo(UPDATED_WARRANTY_ID);
        assertThat(testWarrantyDetails.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testWarrantyDetails.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWarrantyDetails.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testWarrantyDetails.getPayDate()).isEqualTo(UPDATED_PAY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingWarrantyDetails() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();
        warrantyDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warrantyDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarrantyDetails() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();
        warrantyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarrantyDetails() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();
        warrantyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarrantyDetailsWithPatch() throws Exception {
        // Initialize the database
        IWarrantyDetailsRepository.saveAndFlush(warrantyDetails);

        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();

        // Update the warrantyDetails using partial update
        WarrantyDetails partialUpdatedWarrantyDetails = new WarrantyDetails();
        partialUpdatedWarrantyDetails.setId(warrantyDetails.getId());

        partialUpdatedWarrantyDetails.productCode(UPDATED_PRODUCT_CODE).note(UPDATED_NOTE);

        restWarrantyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarrantyDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarrantyDetails))
            )
            .andExpect(status().isOk());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
        WarrantyDetails testWarrantyDetails = warrantyDetailsList.get(warrantyDetailsList.size() - 1);
        assertThat(testWarrantyDetails.getWarrantyId()).isEqualTo(DEFAULT_WARRANTY_ID);
        assertThat(testWarrantyDetails.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testWarrantyDetails.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWarrantyDetails.getReceivedDate()).isEqualTo(DEFAULT_RECEIVED_DATE);
        assertThat(testWarrantyDetails.getPayDate()).isEqualTo(DEFAULT_PAY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateWarrantyDetailsWithPatch() throws Exception {
        // Initialize the database
        IWarrantyDetailsRepository.saveAndFlush(warrantyDetails);

        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();

        // Update the warrantyDetails using partial update
        WarrantyDetails partialUpdatedWarrantyDetails = new WarrantyDetails();
        partialUpdatedWarrantyDetails.setId(warrantyDetails.getId());

        partialUpdatedWarrantyDetails
            .warrantyId(UPDATED_WARRANTY_ID)
            .productCode(UPDATED_PRODUCT_CODE)
            .note(UPDATED_NOTE)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .payDate(UPDATED_PAY_DATE);

        restWarrantyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarrantyDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarrantyDetails))
            )
            .andExpect(status().isOk());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
        WarrantyDetails testWarrantyDetails = warrantyDetailsList.get(warrantyDetailsList.size() - 1);
        assertThat(testWarrantyDetails.getWarrantyId()).isEqualTo(UPDATED_WARRANTY_ID);
        assertThat(testWarrantyDetails.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testWarrantyDetails.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWarrantyDetails.getReceivedDate()).isEqualTo(UPDATED_RECEIVED_DATE);
        assertThat(testWarrantyDetails.getPayDate()).isEqualTo(UPDATED_PAY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingWarrantyDetails() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();
        warrantyDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warrantyDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarrantyDetails() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();
        warrantyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarrantyDetails() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyDetailsRepository.findAll().size();
        warrantyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warrantyDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarrantyDetails in the database
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarrantyDetails() throws Exception {
        // Initialize the database
        IWarrantyDetailsRepository.saveAndFlush(warrantyDetails);

        int databaseSizeBeforeDelete = IWarrantyDetailsRepository.findAll().size();

        // Delete the warrantyDetails
        restWarrantyDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, warrantyDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WarrantyDetails> warrantyDetailsList = IWarrantyDetailsRepository.findAll();
        assertThat(warrantyDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
