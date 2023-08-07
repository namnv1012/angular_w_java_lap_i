package com.laptop.shopping.restful;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.laptop.shopping.IntegrationTest;
import com.laptop.shopping.domain.Warranty;

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
 * Integration tests for the {@link WarrantyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarrantyResourceIT {

    private static final Long DEFAULT_ORDER_ID = 1L;
    private static final Long UPDATED_ORDER_ID = 2L;

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/warranties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private com.laptop.shopping.repository.iRepository.IWarrantyRepository IWarrantyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarrantyMockMvc;

    private Warranty warranty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warranty createEntity(EntityManager em) {
        Warranty warranty = new Warranty()
            .orderId(DEFAULT_ORDER_ID)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .createDate(DEFAULT_CREATE_DATE)
            .createName(DEFAULT_CREATE_NAME)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
        return warranty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warranty createUpdatedEntity(EntityManager em) {
        Warranty warranty = new Warranty()
            .orderId(UPDATED_ORDER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .createName(UPDATED_CREATE_NAME)
            .expirationDate(UPDATED_EXPIRATION_DATE);
        return warranty;
    }

    @BeforeEach
    public void initTest() {
        warranty = createEntity(em);
    }

    @Test
    @Transactional
    void createWarranty() throws Exception {
        int databaseSizeBeforeCreate = IWarrantyRepository.findAll().size();
        // Create the Warranty
        restWarrantyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warranty)))
            .andExpect(status().isCreated());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeCreate + 1);
        Warranty testWarranty = warrantyList.get(warrantyList.size() - 1);
        assertThat(testWarranty.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);
        assertThat(testWarranty.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testWarranty.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testWarranty.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testWarranty.getCreateName()).isEqualTo(DEFAULT_CREATE_NAME);
        assertThat(testWarranty.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void createWarrantyWithExistingId() throws Exception {
        // Create the Warranty with an existing ID
        warranty.setId(1L);

        int databaseSizeBeforeCreate = IWarrantyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarrantyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warranty)))
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWarranties() throws Exception {
        // Initialize the database
        IWarrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warranty.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createName").value(hasItem(DEFAULT_CREATE_NAME)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getWarranty() throws Exception {
        // Initialize the database
        IWarrantyRepository.saveAndFlush(warranty);

        // Get the warranty
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL_ID, warranty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warranty.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID.intValue()))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createName").value(DEFAULT_CREATE_NAME))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWarranty() throws Exception {
        // Get the warranty
        restWarrantyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWarranty() throws Exception {
        // Initialize the database
        IWarrantyRepository.saveAndFlush(warranty);

        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();

        // Update the warranty
        Warranty updatedWarranty = IWarrantyRepository.findById(warranty.getId()).get();
        // Disconnect from session so that the updates on updatedWarranty are not directly saved in db
        em.detach(updatedWarranty);
        updatedWarranty
            .orderId(UPDATED_ORDER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .createName(UPDATED_CREATE_NAME)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWarranty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWarranty))
            )
            .andExpect(status().isOk());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
        Warranty testWarranty = warrantyList.get(warrantyList.size() - 1);
        assertThat(testWarranty.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testWarranty.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testWarranty.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testWarranty.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWarranty.getCreateName()).isEqualTo(UPDATED_CREATE_NAME);
        assertThat(testWarranty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingWarranty() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();
        warranty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warranty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warranty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarranty() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();
        warranty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warranty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarranty() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();
        warranty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warranty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarrantyWithPatch() throws Exception {
        // Initialize the database
        IWarrantyRepository.saveAndFlush(warranty);

        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();

        // Update the warranty using partial update
        Warranty partialUpdatedWarranty = new Warranty();
        partialUpdatedWarranty.setId(warranty.getId());

        partialUpdatedWarranty
            .orderId(UPDATED_ORDER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createName(UPDATED_CREATE_NAME);

        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarranty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarranty))
            )
            .andExpect(status().isOk());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
        Warranty testWarranty = warrantyList.get(warrantyList.size() - 1);
        assertThat(testWarranty.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testWarranty.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testWarranty.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testWarranty.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testWarranty.getCreateName()).isEqualTo(UPDATED_CREATE_NAME);
        assertThat(testWarranty.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateWarrantyWithPatch() throws Exception {
        // Initialize the database
        IWarrantyRepository.saveAndFlush(warranty);

        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();

        // Update the warranty using partial update
        Warranty partialUpdatedWarranty = new Warranty();
        partialUpdatedWarranty.setId(warranty.getId());

        partialUpdatedWarranty
            .orderId(UPDATED_ORDER_ID)
            .customerName(UPDATED_CUSTOMER_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .createDate(UPDATED_CREATE_DATE)
            .createName(UPDATED_CREATE_NAME)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarranty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarranty))
            )
            .andExpect(status().isOk());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
        Warranty testWarranty = warrantyList.get(warrantyList.size() - 1);
        assertThat(testWarranty.getOrderId()).isEqualTo(UPDATED_ORDER_ID);
        assertThat(testWarranty.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testWarranty.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testWarranty.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWarranty.getCreateName()).isEqualTo(UPDATED_CREATE_NAME);
        assertThat(testWarranty.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingWarranty() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();
        warranty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warranty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warranty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarranty() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();
        warranty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warranty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarranty() throws Exception {
        int databaseSizeBeforeUpdate = IWarrantyRepository.findAll().size();
        warranty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(warranty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warranty in the database
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarranty() throws Exception {
        // Initialize the database
        IWarrantyRepository.saveAndFlush(warranty);

        int databaseSizeBeforeDelete = IWarrantyRepository.findAll().size();

        // Delete the warranty
        restWarrantyMockMvc
            .perform(delete(ENTITY_API_URL_ID, warranty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Warranty> warrantyList = IWarrantyRepository.findAll();
        assertThat(warrantyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
