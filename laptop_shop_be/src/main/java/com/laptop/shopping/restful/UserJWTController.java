package com.laptop.shopping.restful;

import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.AdminDto;
import com.laptop.shopping.exception.UserNotActivatedException;
import com.laptop.shopping.security.jwt.JWTFilter;
import com.laptop.shopping.security.jwt.JwtResponse;
import com.laptop.shopping.security.jwt.TokenProvider;
import com.laptop.shopping.service.UserService;
import com.laptop.shopping.mapper.UserMapper;
import com.laptop.shopping.restful.viewmodel.LoginViewModel;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserService userService;

    private final UserMapper userMapper;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService, UserMapper userMapper) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginViewModel loginViewModel) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginViewModel.getUsername(),
            loginViewModel.getPassword()
        );

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, loginViewModel.isRememberMe());
            User user = userService.getUserWithAuthoritiesByLogin(loginViewModel.getUsername()).orElse(new User());
            AdminDto userDTO = userMapper.userToAdminUserDTO(user);
            JwtResponse jwtResponse = new JwtResponse(jwt, userDTO);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(jwtResponse, httpHeaders, HttpStatus.OK);
        } catch (UserNotActivatedException e) {
            throw new UserNotActivatedException(e.getMessage());
        } catch (InternalAuthenticationServiceException eux) {
            throw new UserNotActivatedException("Tài khoản bị khóa");
        } catch (AuthenticationException ex) {
            throw new UserNotActivatedException("Đăng nhập không thành công");
        }
    }

}
