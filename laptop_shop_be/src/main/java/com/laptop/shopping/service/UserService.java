package com.laptop.shopping.service;

import com.laptop.shopping.common.Constants;
import com.laptop.shopping.domain.Authority;
import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.AdminDto;
import com.laptop.shopping.dto.SearchUser;
import com.laptop.shopping.dto.UserDto;
import com.laptop.shopping.exception.service.EmailAlreadyUsedException;
import com.laptop.shopping.exception.service.InvalidPasswordException;
import com.laptop.shopping.exception.service.UsernameAlreadyUsedException;
import com.laptop.shopping.repository.iRepository.IAuthorRepository;
import com.laptop.shopping.repository.iRepository.IUserRepository;
import com.laptop.shopping.repository.iRepository.custom.IUserCustomRepository;
import com.laptop.shopping.security.AuthoritiesConstants;
import com.laptop.shopping.security.SecurityUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final com.laptop.shopping.repository.iRepository.IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.laptop.shopping.repository.iRepository.IAuthorRepository IAuthorRepository;
    private final com.laptop.shopping.repository.iRepository.custom.IUserCustomRepository IUserCustomRepository;

    public UserService(IUserRepository IUserRepository, PasswordEncoder passwordEncoder, IAuthorRepository IAuthorRepository, IUserCustomRepository IUserCustomRepository) {
        this.IUserRepository = IUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.IAuthorRepository = IAuthorRepository;
        this.IUserCustomRepository = IUserCustomRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return IUserRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return IUserRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minus(1, ChronoUnit.DAYS)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return IUserRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            });
    }

    public User registerUser(AdminDto userDTO, String password) {
        IUserRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        IUserRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        IAuthorRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        IUserRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        IUserRepository.delete(existingUser);
        IUserRepository.flush();
        return true;
    }

    public User createUser(AdminDto userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(IAuthorRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        IUserRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminDto> updateUser(AdminDto userDTO) {
        return Optional
            .of(IUserRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO
                    .getAuthorities()
                    .stream()
                    .map(IAuthorRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(AdminDto::new);
    }

    public void deleteUser(String login) {
        IUserRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                IUserRepository.delete(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(IUserRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(IUserRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminDto> getAllManagedUsers(Pageable pageable) {
        return IUserRepository.findAll(pageable).map(AdminDto::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllPublicUsers(Pageable pageable) {
        return IUserRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDto::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return IUserRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(IUserRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        IUserRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                IUserRepository.delete(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return IAuthorRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public List<UserDto> getListUserByRole(String roleName) {
        return IUserCustomRepository.getListUserByRole(roleName);
    }

    @Transactional
    public User save(User user) {
        String passwordEncoded;
        if (user.getPassword() != null) passwordEncoded = this.passwordEncoder.encode(user.getPassword());
        else passwordEncoded =
            this.passwordEncoder.encode("123456");
        user.setPassword(passwordEncoded);
        return this.IUserRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        User updated = this.IUserRepository.save(user);
        return updated;
    }

    public Optional<User> findByLogin(String login) {
        return this.IUserRepository.findOneByLogin(login);
    }

    public Map<String, Object> searchCustomer(SearchUser searchUser, Integer page, Integer pageSize) {
        List<UserDto> lstUser = IUserCustomRepository.searchListCustomer(searchUser, page, pageSize);
        Integer total = IUserCustomRepository.totalListCustomer(searchUser).size();
        Map<String, Object> result = new HashMap<>();
        result.put("lstUser", lstUser);
        result.put("total", total);
        return result;
    }
}
