package com.laptop.shopping.restful;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.laptop.shopping.IntegrationTest;
import com.laptop.shopping.domain.Producer;

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
 * Integration tests for the {@link ProducerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProducerResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UPDATE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/producers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private com.laptop.shopping.repository.iRepository.IProducerRepository IProducerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProducerMockMvc;

    private Producer producer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producer createEntity(EntityManager em) {
        Producer producer = new Producer()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .createName(DEFAULT_CREATE_NAME)
            .updateDate(DEFAULT_UPDATE_DATE)
            .updateName(DEFAULT_UPDATE_NAME);
        return producer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producer createUpdatedEntity(EntityManager em) {
        Producer producer = new Producer()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createName(UPDATED_CREATE_NAME)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateName(UPDATED_UPDATE_NAME);
        return producer;
    }

    @BeforeEach
    public void initTest() {
        producer = createEntity(em);
    }

    @Test
    @Transactional
    void createProducer() throws Exception {
        int databaseSizeBeforeCreate = IProducerRepository.findAll().size();
        // Create the Producer
        restProducerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isCreated());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeCreate + 1);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProducer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProducer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProducer.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testProducer.getCreateName()).isEqualTo(DEFAULT_CREATE_NAME);
        assertThat(testProducer.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testProducer.getUpdateName()).isEqualTo(DEFAULT_UPDATE_NAME);
    }

    @Test
    @Transactional
    void createProducerWithExistingId() throws Exception {
        // Create the Producer with an existing ID
        producer.setId(1L);

        int databaseSizeBeforeCreate = IProducerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProducerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProducers() throws Exception {
        // Initialize the database
        IProducerRepository.saveAndFlush(producer);

        // Get all the producerList
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createName").value(hasItem(DEFAULT_CREATE_NAME)))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateName").value(hasItem(DEFAULT_UPDATE_NAME)));
    }

    @Test
    @Transactional
    void getProducer() throws Exception {
        // Initialize the database
        IProducerRepository.saveAndFlush(producer);

        // Get the producer
        restProducerMockMvc
            .perform(get(ENTITY_API_URL_ID, producer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producer.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createName").value(DEFAULT_CREATE_NAME))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.updateName").value(DEFAULT_UPDATE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingProducer() throws Exception {
        // Get the producer
        restProducerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProducer() throws Exception {
        // Initialize the database
        IProducerRepository.saveAndFlush(producer);

        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();

        // Update the producer
        Producer updatedProducer = IProducerRepository.findById(producer.getId()).get();
        // Disconnect from session so that the updates on updatedProducer are not directly saved in db
        em.detach(updatedProducer);
        updatedProducer
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createName(UPDATED_CREATE_NAME)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateName(UPDATED_UPDATE_NAME);

        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProducer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProducer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProducer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProducer.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testProducer.getCreateName()).isEqualTo(UPDATED_CREATE_NAME);
        assertThat(testProducer.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testProducer.getUpdateName()).isEqualTo(UPDATED_UPDATE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProducer() throws Exception {
        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, producer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducer() throws Exception {
        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducer() throws Exception {
        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProducerWithPatch() throws Exception {
        // Initialize the database
        IProducerRepository.saveAndFlush(producer);

        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();

        // Update the producer using partial update
        Producer partialUpdatedProducer = new Producer();
        partialUpdatedProducer.setId(producer.getId());

        partialUpdatedProducer.createDate(UPDATED_CREATE_DATE).createName(UPDATED_CREATE_NAME).updateName(UPDATED_UPDATE_NAME);

        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProducer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProducer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProducer.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testProducer.getCreateName()).isEqualTo(UPDATED_CREATE_NAME);
        assertThat(testProducer.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testProducer.getUpdateName()).isEqualTo(UPDATED_UPDATE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProducerWithPatch() throws Exception {
        // Initialize the database
        IProducerRepository.saveAndFlush(producer);

        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();

        // Update the producer using partial update
        Producer partialUpdatedProducer = new Producer();
        partialUpdatedProducer.setId(producer.getId());

        partialUpdatedProducer
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createName(UPDATED_CREATE_NAME)
            .updateDate(UPDATED_UPDATE_DATE)
            .updateName(UPDATED_UPDATE_NAME);

        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProducer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProducer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProducer.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testProducer.getCreateName()).isEqualTo(UPDATED_CREATE_NAME);
        assertThat(testProducer.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testProducer.getUpdateName()).isEqualTo(UPDATED_UPDATE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProducer() throws Exception {
        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, producer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducer() throws Exception {
        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(producer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducer() throws Exception {
        int databaseSizeBeforeUpdate = IProducerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(producer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producer in the database
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducer() throws Exception {
        // Initialize the database
        IProducerRepository.saveAndFlush(producer);

        int databaseSizeBeforeDelete = IProducerRepository.findAll().size();

        // Delete the producer
        restProducerMockMvc
            .perform(delete(ENTITY_API_URL_ID, producer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Producer> producerList = IProducerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
