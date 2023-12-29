package com.B2B.Portal.product.service;

import com.B2B.Portal.product.dto.ProductDTO;
import com.B2B.Portal.product.exception.InvalidSupplierException;
import com.B2B.Portal.product.exception.ProductNotFoundException;
import com.B2B.Portal.product.model.Product;
import com.B2B.Portal.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    private final String userServiceUrl = "http://localhost:8080/api/v1/users/validate-supplier";

    @Autowired
    public ProductService(RestTemplateBuilder restTemplateBuilder, ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.modelMapper = modelMapper;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        if (!isUserValidSupplier(productDTO.getSupplierId())) {
            throw new InvalidSupplierException("No valid supplier found with the provided user ID.");
        }
        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    private boolean isUserValidSupplier(Long userId) {
        try {
            String url = userServiceUrl + "?userId=" + userId;
            Boolean isValid = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(isValid);
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        return modelMapper.map(product, ProductDTO.class);
    }

    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
        modelMapper.map(productDTO, product);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        productRepository.deleteById(productId);
    }

    public List<ProductDTO> getProductsBySupplierId(Long supplierId) {
        List<Product> products = productRepository.findBySupplierId(supplierId);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    public ProductDTO updateProductStatus(Long productId, String newStatus) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        product.setStatus(newStatus);
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    // Additional methods for handling product status updates can be added here
}
