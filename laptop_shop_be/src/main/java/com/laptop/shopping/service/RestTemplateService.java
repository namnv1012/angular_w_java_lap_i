package com.laptop.shopping.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestTemplateService {
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
