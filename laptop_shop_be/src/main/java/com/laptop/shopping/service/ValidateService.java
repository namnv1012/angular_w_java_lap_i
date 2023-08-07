package com.laptop.shopping.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ValidateService {
    public Map<String, String> getErrorValidate(BindingResult result) {
        Map<String, String> errorValidates = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorValidates.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorValidates;
    }
}
