package com.B2B.Portal.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long orderId;

    @NotNull(message = "Customer ID is mandatory")
    private String customerId;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemDTO> items;

    @NotNull(message = "Order date is mandatory")
    private LocalDateTime orderDate;

    @NotBlank(message = "Delivery address is mandatory")
    private String deliveryAddress;

    private LocalDateTime deliveryDate; // Assuming this can be null

    @NotBlank(message = "Order status is mandatory")
    private String status;


    @NotBlank(message = "Customer Name is mandatory")
    private String customerName;

    public OrderDTO() {
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }





    public static class OrderItemDTO {

        @NotNull(message = "Product ID is mandatory")
        private Long productId;

        public Long getProductId() {
            return productId;
        }

        @NotNull(message = "Product Description is mandatory")
        private String productDescription;
        @NotNull(message = "Quantity is mandatory")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;

        @NotNull(message = "Price is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        private Double price;

        @NotNull(message = "Supplier ID is mandatory")
        private String supplierId;



        @NotNull(message = "Supplier Name is mandatory")
        private String supplierName;



        public OrderItemDTO() {
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }


        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }



    }
}
