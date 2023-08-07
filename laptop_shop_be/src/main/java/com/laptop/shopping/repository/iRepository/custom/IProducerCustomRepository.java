package com.laptop.shopping.repository.iRepository.custom;

import com.laptop.shopping.dto.ProducerDto;
import com.laptop.shopping.dto.SearchProducer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProducerCustomRepository {
    List<ProducerDto> search(SearchProducer searchProducer, Integer page, Integer pageSize);
    List<ProducerDto> getListBySeaProducer(SearchProducer searchProducer);
}
