package com.laptop.shopping.service.iService;

import com.laptop.shopping.dto.SearchProducer;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IProducerService {
    Map<String, Object> searchProducer(SearchProducer searchProducer, Integer page, Integer pageSize);
}
