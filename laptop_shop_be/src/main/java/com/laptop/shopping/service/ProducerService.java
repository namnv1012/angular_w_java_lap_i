package com.laptop.shopping.service;

import com.laptop.shopping.dto.ProducerDto;
import com.laptop.shopping.dto.SearchProducer;
import com.laptop.shopping.repository.iRepository.custom.IProducerCustomRepository;
import com.laptop.shopping.service.iService.IProducerService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProducerService implements IProducerService {

    private final com.laptop.shopping.repository.iRepository.custom.IProducerCustomRepository IProducerCustomRepository;

    public ProducerService(IProducerCustomRepository IProducerCustomRepository) {
        this.IProducerCustomRepository = IProducerCustomRepository;
    }

    @Override
    public Map<String, Object> searchProducer(SearchProducer searchProducer, Integer page, Integer pageSize) {
        List<ProducerDto> lstProducer = IProducerCustomRepository.search(searchProducer, page, pageSize);
        Integer total = IProducerCustomRepository.getListBySeaProducer(searchProducer).size();
        Map<String, Object> result = new HashMap<>();
        result.put("lstProducer", lstProducer);
        result.put("total", total);
        return result;
    }
}
