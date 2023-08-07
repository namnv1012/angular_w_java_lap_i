package com.laptop.shopping.service;

import com.laptop.shopping.common.FileUtil;
import com.laptop.shopping.domain.Product;
import com.laptop.shopping.domain.User;
import com.laptop.shopping.dto.ProductDto;
import com.laptop.shopping.dto.SearchProducer;
import com.laptop.shopping.dto.ServiceResult;
import com.laptop.shopping.mapper.ProductCustomMapper;
import com.laptop.shopping.mapper.ProductMapper;
import com.laptop.shopping.repository.iRepository.IImageRepository;
import com.laptop.shopping.repository.iRepository.IProductRepository;
import com.laptop.shopping.repository.iRepository.custom.IProductCustomRepository;
import com.laptop.shopping.service.iService.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Component
public class ProductService implements IProductService {

    @Value("${save-image.path-img}")
    private String folderImage;

    @Value("${save-image.path-img2}")
    private String folderImage2;

    private final com.laptop.shopping.repository.iRepository.IProductRepository IProductRepository;
    private final IProductCustomRepository productCustomRepository;

    @Autowired
    private ProductMapper productMapper;

    private final com.laptop.shopping.repository.iRepository.IImageRepository IImageRepository;

    private final UserService userService;

    private final com.laptop.shopping.repository.iRepository.custom.IProductCustomRepository IProductCustomRepository;

    public ProductService(IProductRepository IProductRepository, com.laptop.shopping.repository.iRepository.custom.IProductCustomRepository productCustomRepository, IImageRepository IImageRepository, UserService userService, IProductCustomRepository IProductCustomRepository) {
        this.IProductRepository = IProductRepository;
        this.productCustomRepository = productCustomRepository;
        this.IImageRepository = IImageRepository;
        this.userService = userService;
        this.IProductCustomRepository = IProductCustomRepository;
    }

    @Override
    public ServiceResult<Product> save(ProductDto productDTO, MultipartFile fileImageProduct, MultipartFile fileImageProduct2, MultipartFile fileImageProduct3) throws IOException {
        ServiceResult<Product> result = new ServiceResult<>();
        try {
            Product product = productMapper.toEntity(productDTO);
            Optional<User> userLogin = userService.getUserWithAuthorities();
            if (product.getId() != null) {
                Product oldProduct = IProductRepository.getProductById(product.getId());
                product.setCreateDate(oldProduct.getCreateDate());
                product.setCreateName(oldProduct.getCreateName());
                product.setUpdateDate(Instant.now());
                product.setQuantity(oldProduct.getQuantity());
                if (userLogin.isPresent()) {
                    product.setUpdateName(userLogin.get().getLogin());
                }
            } else {
                product.setQuantity(0);
                product.setCreateDate(Instant.now());
                if (userLogin.isPresent()) {
                    product.setCreateName(userLogin.get().getLogin());
                }
            }
            // Lưu ảnh của sản phẩm
            if (fileImageProduct != null) {
                String nameFile = fileImageProduct.getOriginalFilename();
                String fileInputPath = this.folderImage + File.separator + nameFile;
                String fileInputPath2 = this.folderImage2 + File.separator + nameFile;
                FileUtil.saveFile(
                    fileInputPath, fileImageProduct
                );
                FileUtil.saveFile(
                    fileInputPath2, fileImageProduct
                );
                product.setImageUrl(FileUtil.formatPathImg(fileInputPath));
            }
            if (fileImageProduct2 != null) {
                String nameFile = fileImageProduct2.getOriginalFilename();
                String fileInputPath = this.folderImage + File.separator + nameFile;
                String fileInputPath2 = this.folderImage2 + File.separator + nameFile;
                FileUtil.saveFile(
                    fileInputPath, fileImageProduct2
                );
                FileUtil.saveFile(
                    fileInputPath2, fileImageProduct2
                );
                product.setImageUrl2(FileUtil.formatPathImg(fileInputPath));
            }
            if (fileImageProduct3 != null) {
                String nameFile = fileImageProduct3.getOriginalFilename();
                String fileInputPath = this.folderImage + File.separator + nameFile;
                String fileInputPath2 = this.folderImage2 + File.separator + nameFile;
                FileUtil.saveFile(
                    fileInputPath, fileImageProduct3
                );
                FileUtil.saveFile(
                    fileInputPath2, fileImageProduct3
                );
                product.setImageUrl3(FileUtil.formatPathImg(fileInputPath));
            }
            product.setStatus(true);
            product = IProductRepository.save(product);
            result.setMessage("Thành công");
            result.setStatus(HttpStatus.OK);
            result.setData(product);
        } catch (Exception ex) {
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("Thất bại");
        }
        return result;
    }

    @Override
    public Map<String, Object> loadDataHome() {
        List<Product> lstCreateDate = IProductRepository.getListProductOrderCreateDate();
        List<Product> lstProductSale = IProductRepository.getListProductOrderSale();
        Map<String, Object> result = new HashMap<>();
        result.put("lstProductNew", lstCreateDate);
        result.put("lstProductSale", lstProductSale);
        return result;
    }

    @Override
    public ProductDto findByProductId(Long id) {
        Optional<Product> product = IProductRepository.findById(id);
        ProductDto productDTO = new ProductDto();
        if (product.isPresent()) {
            productDTO = ProductCustomMapper.toDto(product.get());
        }
        return productDTO;
    }

    @Override
    public Map<String, Object> search(SearchProducer searchProducer, Integer page, Integer pageSize) {
        List<ProductDto> lstProduct = IProductCustomRepository.search(searchProducer, page, pageSize);
        Integer total = IProductCustomRepository.getTotal(searchProducer).size();
        Map<String, Object> result = new HashMap<>();
        result.put("lstProduct", lstProduct);
        result.put("total", total);
        return result;
    }

    @Override
    public Product importQuantity(ProductDto productDTO) {
        Product product = IProductRepository.findByCode(productDTO.getCode());
        if (product.getQuantity() != null) {
            Integer quantity = product.getQuantity() + productDTO.getQuantity();
            product.setQuantity(quantity);
        } else {
            product.setQuantity(productDTO.getQuantity());
        }
        product = IProductRepository.save(product);
        return product;
    }

    @Override
    public List<ProductDto> getAllProductInventory() {
        return productCustomRepository.getAllProductIventory();
    }
}
