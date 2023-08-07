package com.laptop.shopping.service.iService;

import com.laptop.shopping.domain.Product;
import com.laptop.shopping.dto.ProductDto;
import com.laptop.shopping.dto.SearchProducer;
import com.laptop.shopping.dto.ServiceResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface IProductService {
    ServiceResult<Product> save(ProductDto productDTO, MultipartFile fileImageProduct, MultipartFile fileImageProduct2, MultipartFile fileImageProduct3) throws IOException;
    Map<String, Object> loadDataHome();
    ProductDto findByProductId(Long id);
    Map<String, Object> search(SearchProducer searchProducer, Integer page, Integer pageSize);
    Product importQuantity(ProductDto productDTO);
    List<ProductDto> getAllProductInventory();
}
