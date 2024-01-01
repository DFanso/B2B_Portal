package com.B2B.Portal.product.controller;

import com.B2B.Portal.product.dto.ProductDTO;
import com.B2B.Portal.product.exception.InvalidSupplierException;
import com.B2B.Portal.product.model.Product;
import com.B2B.Portal.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        try{
            productDTO.setStatus("PENDING");
            ProductDTO product = productService.createProduct(productDTO);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        }
        catch (InvalidSupplierException e){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get products by supplier ID
    @GetMapping(value = "/supplier/{supplierId}")
    public ResponseEntity<List<ProductDTO>> getProductsBySupplierId(@PathVariable String supplierId) {
        List<ProductDTO> products = productService.getProductsBySupplierId(supplierId);
        return ResponseEntity.ok(products);
    }

    // Get product by product ID
    @GetMapping(value="/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }


    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProductStatus(@PathVariable Long productId, @RequestBody String status) {
        ProductDTO updatedProduct = productService.updateProductStatus(productId, status);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
