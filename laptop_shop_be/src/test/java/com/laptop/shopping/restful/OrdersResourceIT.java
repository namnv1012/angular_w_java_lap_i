package com.laptop.shopping.restful;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.laptop.shopping.IntegrationTest;
import com.laptop.shopping.domain.Orders;

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
 * Integration tests for the {@link OrdersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdersResourceIT {

    private static final String DEFAULT_USER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_USER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private com.laptop.shopping.repository.iRepository.IOrdersRepository IOrdersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdersMockMvc;

    private Orders orders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createEntity(EntityManager em) {
        Orders orders = new Orders()
            .userCode(DEFAULT_USER_CODE)
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .note(DEFAULT_NOTE)
            .status(DEFAULT_STATUS)
            .orderDate(DEFAULT_ORDER_DATE);
        return orders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createUpdatedEntity(EntityManager em) {
        Orders orders = new Orders()
            .userCode(UPDATED_USER_CODE)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE);
        return orders;
    }

    @BeforeEach
    public void initTest() {
        orders = createEntity(em);
    }

    @Test
    @Transactional
    void createOrders() throws Exception {
        int databaseSizeBeforeCreate = IOrdersRepository.findAll().size();
        // Create the Orders
        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getUserCode()).isEqualTo(DEFAULT_USER_CODE);
        assertThat(testOrders.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrders.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testOrders.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrders.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrders.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testOrders.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrders.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
    }

    @Test
    @Transactional
    void createOrdersWithExistingId() throws Exception {
        // Create the Orders with an existing ID
        orders.setId(1L);

        int databaseSizeBeforeCreate = IOrdersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        IOrdersRepository.saveAndFlush(orders);

        // Get all the ordersList
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
            .andExpect(jsonPath("$.[*].userCode").value(hasItem(DEFAULT_USER_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrders() throws Exception {
        // Initialize the database
        IOrdersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc
            .perform(get(ENTITY_API_URL_ID, orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.userCode").value(DEFAULT_USER_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrders() throws Exception {
        // Initialize the database
        IOrdersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = IOrdersRepository.findById(orders.getId()).get();
        // Disconnect from session so that the updates on updatedOrders are not directly saved in db
        em.detach(updatedOrders);
        updatedOrders
            .userCode(UPDATED_USER_CODE)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE);

        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getUserCode()).isEqualTo(UPDATED_USER_CODE);
        assertThat(testOrders.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrders.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOrders.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrders.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrders.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrders.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void putNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orders.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrders() throws Exception {
        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrders() throws Exception {
        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdersWithPatch() throws Exception {
        // Initialize the database
        IOrdersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();

        // Update the orders using partial update
        Orders partialUpdatedOrders = new Orders();
        partialUpdatedOrders.setId(orders.getId());

        partialUpdatedOrders.userCode(UPDATED_USER_CODE).phone(UPDATED_PHONE).address(UPDATED_ADDRESS).status(UPDATED_STATUS);

        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getUserCode()).isEqualTo(UPDATED_USER_CODE);
        assertThat(testOrders.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrders.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOrders.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrders.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrders.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrders.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
    }

    @Test
    @Transactional
    void fullUpdateOrdersWithPatch() throws Exception {
        // Initialize the database
        IOrdersRepository.saveAndFlush(orders);

        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();

        // Update the orders using partial update
        Orders partialUpdatedOrders = new Orders();
        partialUpdatedOrders.setId(orders.getId());

        partialUpdatedOrders
            .userCode(UPDATED_USER_CODE)
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .note(UPDATED_NOTE)
            .status(UPDATED_STATUS)
            .orderDate(UPDATED_ORDER_DATE);

        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrders))
            )
            .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = ordersList.get(ordersList.size() - 1);
        assertThat(testOrders.getUserCode()).isEqualTo(UPDATED_USER_CODE);
        assertThat(testOrders.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrders.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOrders.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrders.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrders.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testOrders.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrders.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrders() throws Exception {
        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrders() throws Exception {
        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orders))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrders() throws Exception {
        int databaseSizeBeforeUpdate = IOrdersRepository.findAll().size();
        orders.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(orders)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orders in the database
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrders() throws Exception {
        // Initialize the database
        IOrdersRepository.saveAndFlush(orders);

        int databaseSizeBeforeDelete = IOrdersRepository.findAll().size();

        // Delete the orders
        restOrdersMockMvc
            .perform(delete(ENTITY_API_URL_ID, orders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orders> ordersList = IOrdersRepository.findAll();
        assertThat(ordersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
