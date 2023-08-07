package com.laptop.shopping.restful;

import com.laptop.shopping.domain.Authority;
import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.AdminDto;
import com.laptop.shopping.dto.PasswordChangeDto;
import com.laptop.shopping.dto.SearchUser;
import com.laptop.shopping.exception.restful.EmailAlreadyUsedException;
import com.laptop.shopping.exception.restful.InvalidPasswordException;
import com.laptop.shopping.repository.iRepository.IUserRepository;
import com.laptop.shopping.security.SecurityUtils;
import com.laptop.shopping.service.MailService;
import com.laptop.shopping.service.UserService;
import com.laptop.shopping.mapper.UserMapper;
import com.laptop.shopping.restful.viewmodel.KeyAndPasswordViewModel;
import com.laptop.shopping.restful.viewmodel.ManagedUserViewModel;

import java.time.Instant;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AccountResource {
    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);
    private final com.laptop.shopping.repository.iRepository.IUserRepository IUserRepository;
    private final UserService userService;
    private final MailService mailService;
    private final UserMapper userMapper;

    public AccountResource(IUserRepository IUserRepository, UserService userService, MailService mailService, UserMapper userMapper) {
        this.IUserRepository = IUserRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserViewModel managedUserViewModel) {
        if (isPasswordLengthInvalid(managedUserViewModel.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserViewModel, managedUserViewModel.getPassword());
        mailService.sendActivationEmail(user);
    }

    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    @GetMapping("/account")
    public AdminDto getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminDto::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminDto userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = IUserRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = IUserRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.get());
        } else {
            log.warn("Password reset requested for non existing mail");
        }
    }

    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordViewModel keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    @PostMapping("/account/register-customer")
    public ResponseEntity<?> createCustomer(@RequestBody AdminDto userDTO) {
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = IUserRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = IUserRepository.findOneByLogin(userLogin);
        User userAdd = new User();
        if (user.isPresent()) {
            throw new AccountResourceException("Tài khoản đã tồn tại");
        } else {
            // Lưu tài khoản bệnh nhân
            userAdd = userMapper.userDTOToUser(userDTO);
            Set<Authority> authorities = new HashSet<>();
            userAdd.setCreatedDate(Instant.now());
            userAdd.setCreatedBy(userDTO.getLogin());
            userAdd.setActivated(true);
            userAdd.setPassword(userDTO.getPassword());
            userAdd.setEmail(userDTO.getEmail());
            userAdd.setPhoneNumber(userDTO.getPhoneNumber());
            Authority authority = new Authority();
            authority.setName("ROLE_CUSTOMER");
            authorities.add(authority);
            userAdd.setAuthorities(authorities);
            userAdd = userService.save(userAdd);
        }
        return ResponseEntity.ok().body(userAdd);
    }

    @PostMapping("/account/get-list-customer")
    public ResponseEntity<?> getListCustomer(@RequestBody SearchUser searchUser,
                                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                             @RequestParam(value = "page-size", required = false, defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = userService.searchCustomer(searchUser, page, pageSize);
        return ResponseEntity.ok().body(result);
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
                password.length() < ManagedUserViewModel.PASSWORD_MIN_LENGTH ||
                password.length() > ManagedUserViewModel.PASSWORD_MAX_LENGTH
        );
    }
}
