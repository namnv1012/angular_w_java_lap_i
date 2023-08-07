package com.laptop.shopping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpHeadersService {
    private final HttpServletRequest httpServletRequest;
    public HttpHeaders buildHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.nonNull(auth) && StringUtils.isNotBlank(auth)) {
            httpHeaders.add(HttpHeaders.AUTHORIZATION, auth);
        }
        return httpHeaders;
    }
}
