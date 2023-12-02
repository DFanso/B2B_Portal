package com.B2B.Portal.product.dto;

import jakarta.validation.constraints.*;

public class ProductDTO {

    @NotBlank(message = "Product name is mandatory")
    private String name;

    @NotBlank(message = "Product description is mandatory")
    private String description;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Product status is mandatory")
    private String status;

    @NotNull(message = "Supplier ID is mandatory")
    private Long supplierId;

    @NotNull(message = "Product images are mandatory")
    @Size(min = 1, message = "At least one image URL must be provided")
    private String[] images;

    // Default constructor
    public ProductDTO() {
    }

    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

}
