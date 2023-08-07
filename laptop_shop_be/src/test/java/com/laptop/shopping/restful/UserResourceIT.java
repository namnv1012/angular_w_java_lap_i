package com.laptop.shopping.restful;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.laptop.shopping.repository.iRepository.IUserRepository;
import com.laptop.shopping.IntegrationTest;
import com.laptop.shopping.domain.Authority;
import com.laptop.shopping.domain.User;
import com.laptop.shopping.security.AuthoritiesConstants;
import com.laptop.shopping.dto.AdminDto;
import com.laptop.shopping.mapper.UserMapper;
import com.laptop.shopping.restful.viewmodel.ManagedUserViewModel;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";

    private static final Long DEFAULT_ID = 1L;

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Autowired
    private com.laptop.shopping.repository.iRepository.IUserRepository IUserRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    /**
     * Setups the database with one user.
     */
    public static User initTestUser(IUserRepository IUserRepository, EntityManager em) {
        User user = createEntity(em);
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        return user;
    }

    @BeforeEach
    public void initTest() {
        user = initTestUser(IUserRepository, em);
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        int databaseSizeBeforeCreate = IUserRepository.findAll().size();

        // Create the User
        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setLogin(DEFAULT_LOGIN);
        managedUserViewModel.setPassword(DEFAULT_PASSWORD);
        managedUserViewModel.setFirstName(DEFAULT_FIRSTNAME);
        managedUserViewModel.setLastName(DEFAULT_LASTNAME);
        managedUserViewModel.setEmail(DEFAULT_EMAIL);
        managedUserViewModel.setActivated(true);
        managedUserViewModel.setImageUrl(DEFAULT_IMAGEURL);
        managedUserViewModel.setLangKey(DEFAULT_LANGKEY);
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(
                post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isCreated());

        // Validate the User in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
            User testUser = users.get(users.size() - 1);
            assertThat(testUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
            assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
            assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
            assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
            assertThat(testUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
            assertThat(testUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        });
    }

    @Test
    @Transactional
    void createUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = IUserRepository.findAll().size();

        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setId(DEFAULT_ID);
        managedUserViewModel.setLogin(DEFAULT_LOGIN);
        managedUserViewModel.setPassword(DEFAULT_PASSWORD);
        managedUserViewModel.setFirstName(DEFAULT_FIRSTNAME);
        managedUserViewModel.setLastName(DEFAULT_LASTNAME);
        managedUserViewModel.setEmail(DEFAULT_EMAIL);
        managedUserViewModel.setActivated(true);
        managedUserViewModel.setImageUrl(DEFAULT_IMAGEURL);
        managedUserViewModel.setLangKey(DEFAULT_LANGKEY);
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMockMvc
            .perform(
                post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingLogin() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);
        int databaseSizeBeforeCreate = IUserRepository.findAll().size();

        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setLogin(DEFAULT_LOGIN); // this login should already be used
        managedUserViewModel.setPassword(DEFAULT_PASSWORD);
        managedUserViewModel.setFirstName(DEFAULT_FIRSTNAME);
        managedUserViewModel.setLastName(DEFAULT_LASTNAME);
        managedUserViewModel.setEmail("anothermail@localhost");
        managedUserViewModel.setActivated(true);
        managedUserViewModel.setImageUrl(DEFAULT_IMAGEURL);
        managedUserViewModel.setLangKey(DEFAULT_LANGKEY);
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        restUserMockMvc
            .perform(
                post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void createUserWithExistingEmail() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);
        int databaseSizeBeforeCreate = IUserRepository.findAll().size();

        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setLogin("anotherlogin");
        managedUserViewModel.setPassword(DEFAULT_PASSWORD);
        managedUserViewModel.setFirstName(DEFAULT_FIRSTNAME);
        managedUserViewModel.setLastName(DEFAULT_LASTNAME);
        managedUserViewModel.setEmail(DEFAULT_EMAIL); // this email should already be used
        managedUserViewModel.setActivated(true);
        managedUserViewModel.setImageUrl(DEFAULT_IMAGEURL);
        managedUserViewModel.setLangKey(DEFAULT_LANGKEY);
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        // Create the User
        restUserMockMvc
            .perform(
                post("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the User in the database
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeCreate));
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);

        // Get all the users
        restUserMockMvc
            .perform(get("/api/admin/users?sort=id,desc").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGEURL)))
            .andExpect(jsonPath("$.[*].langKey").value(hasItem(DEFAULT_LANGKEY)));
    }

    @Test
    @Transactional
    void getUser() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);

        // Get the user
        restUserMockMvc
            .perform(get("/api/admin/users/{login}", user.getLogin()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGEURL))
            .andExpect(jsonPath("$.langKey").value(DEFAULT_LANGKEY));
    }

    @Test
    @Transactional
    void getNonExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/admin/users/unknown")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateUser() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);
        int databaseSizeBeforeUpdate = IUserRepository.findAll().size();

        // Update the user
        User updatedUser = IUserRepository.findById(user.getId()).get();

        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setId(updatedUser.getId());
        managedUserViewModel.setLogin(updatedUser.getLogin());
        managedUserViewModel.setPassword(UPDATED_PASSWORD);
        managedUserViewModel.setFirstName(UPDATED_FIRSTNAME);
        managedUserViewModel.setLastName(UPDATED_LASTNAME);
        managedUserViewModel.setEmail(UPDATED_EMAIL);
        managedUserViewModel.setActivated(updatedUser.isActivated());
        managedUserViewModel.setImageUrl(UPDATED_IMAGEURL);
        managedUserViewModel.setLangKey(UPDATED_LANGKEY);
        managedUserViewModel.setCreatedBy(updatedUser.getCreatedBy());
        managedUserViewModel.setCreatedDate(updatedUser.getCreatedDate());
        managedUserViewModel.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserViewModel.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(
                put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeUpdate);
            User testUser = users.stream().filter(usr -> usr.getId().equals(updatedUser.getId())).findFirst().get();
            assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
            assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
            assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
            assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
            assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
        });
    }

    @Test
    @Transactional
    void updateUserLogin() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);
        int databaseSizeBeforeUpdate = IUserRepository.findAll().size();

        // Update the user
        User updatedUser = IUserRepository.findById(user.getId()).get();

        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setId(updatedUser.getId());
        managedUserViewModel.setLogin(UPDATED_LOGIN);
        managedUserViewModel.setPassword(UPDATED_PASSWORD);
        managedUserViewModel.setFirstName(UPDATED_FIRSTNAME);
        managedUserViewModel.setLastName(UPDATED_LASTNAME);
        managedUserViewModel.setEmail(UPDATED_EMAIL);
        managedUserViewModel.setActivated(updatedUser.isActivated());
        managedUserViewModel.setImageUrl(UPDATED_IMAGEURL);
        managedUserViewModel.setLangKey(UPDATED_LANGKEY);
        managedUserViewModel.setCreatedBy(updatedUser.getCreatedBy());
        managedUserViewModel.setCreatedDate(updatedUser.getCreatedDate());
        managedUserViewModel.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserViewModel.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(
                put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isOk());

        // Validate the User in the database
        assertPersistedUsers(users -> {
            assertThat(users).hasSize(databaseSizeBeforeUpdate);
            User testUser = users.stream().filter(usr -> usr.getId().equals(updatedUser.getId())).findFirst().get();
            assertThat(testUser.getLogin()).isEqualTo(UPDATED_LOGIN);
            assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRSTNAME);
            assertThat(testUser.getLastName()).isEqualTo(UPDATED_LASTNAME);
            assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
            assertThat(testUser.getImageUrl()).isEqualTo(UPDATED_IMAGEURL);
            assertThat(testUser.getLangKey()).isEqualTo(UPDATED_LANGKEY);
        });
    }

    @Test
    @Transactional
    void updateUserExistingEmail() throws Exception {
        // Initialize the database with 2 users
        IUserRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        IUserRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = IUserRepository.findById(user.getId()).get();

        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setId(updatedUser.getId());
        managedUserViewModel.setLogin(updatedUser.getLogin());
        managedUserViewModel.setPassword(updatedUser.getPassword());
        managedUserViewModel.setFirstName(updatedUser.getFirstName());
        managedUserViewModel.setLastName(updatedUser.getLastName());
        managedUserViewModel.setEmail("jhipster@localhost"); // this email should already be used by anotherUser
        managedUserViewModel.setActivated(updatedUser.isActivated());
        managedUserViewModel.setImageUrl(updatedUser.getImageUrl());
        managedUserViewModel.setLangKey(updatedUser.getLangKey());
        managedUserViewModel.setCreatedBy(updatedUser.getCreatedBy());
        managedUserViewModel.setCreatedDate(updatedUser.getCreatedDate());
        managedUserViewModel.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserViewModel.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(
                put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void updateUserExistingLogin() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);

        User anotherUser = new User();
        anotherUser.setLogin("jhipster");
        anotherUser.setPassword(RandomStringUtils.random(60));
        anotherUser.setActivated(true);
        anotherUser.setEmail("jhipster@localhost");
        anotherUser.setFirstName("java");
        anotherUser.setLastName("hipster");
        anotherUser.setImageUrl("");
        anotherUser.setLangKey("en");
        IUserRepository.saveAndFlush(anotherUser);

        // Update the user
        User updatedUser = IUserRepository.findById(user.getId()).get();

        ManagedUserViewModel managedUserViewModel = new ManagedUserViewModel();
        managedUserViewModel.setId(updatedUser.getId());
        managedUserViewModel.setLogin("jhipster"); // this login should already be used by anotherUser
        managedUserViewModel.setPassword(updatedUser.getPassword());
        managedUserViewModel.setFirstName(updatedUser.getFirstName());
        managedUserViewModel.setLastName(updatedUser.getLastName());
        managedUserViewModel.setEmail(updatedUser.getEmail());
        managedUserViewModel.setActivated(updatedUser.isActivated());
        managedUserViewModel.setImageUrl(updatedUser.getImageUrl());
        managedUserViewModel.setLangKey(updatedUser.getLangKey());
        managedUserViewModel.setCreatedBy(updatedUser.getCreatedBy());
        managedUserViewModel.setCreatedDate(updatedUser.getCreatedDate());
        managedUserViewModel.setLastModifiedBy(updatedUser.getLastModifiedBy());
        managedUserViewModel.setLastModifiedDate(updatedUser.getLastModifiedDate());
        managedUserViewModel.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        restUserMockMvc
            .perform(
                put("/api/admin/users").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(managedUserViewModel))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteUser() throws Exception {
        // Initialize the database
        IUserRepository.saveAndFlush(user);
        int databaseSizeBeforeDelete = IUserRepository.findAll().size();

        // Delete the user
        restUserMockMvc
            .perform(delete("/api/admin/users/{login}", user.getLogin()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        assertPersistedUsers(users -> assertThat(users).hasSize(databaseSizeBeforeDelete - 1));
    }

    @Test
    void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = new User();
        user1.setId(DEFAULT_ID);
        User user2 = new User();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId(2L);
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    void testUserDTOtoUser() {
        AdminDto userDTO = new AdminDto();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setCreatedBy(DEFAULT_LOGIN);
        userDTO.setLastModifiedBy(DEFAULT_LOGIN);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.isActivated()).isTrue();
        assertThat(user.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    void testUserToUserDTO() {
        user.setId(DEFAULT_ID);
        user.setCreatedBy(DEFAULT_LOGIN);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy(DEFAULT_LOGIN);
        user.setLastModifiedDate(Instant.now());
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        user.setAuthorities(authorities);

        AdminDto userDTO = userMapper.userToAdminUserDTO(user);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isTrue();
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    @Test
    void testAuthorityEquals() {
        Authority authorityA = new Authority();
        assertThat(authorityA).isNotEqualTo(null).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isZero();
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isEqualTo(authorityB).hasSameHashCodeAs(authorityB);
    }

    private void assertPersistedUsers(Consumer<List<User>> userAssertion) {
        userAssertion.accept(IUserRepository.findAll());
    }
}
