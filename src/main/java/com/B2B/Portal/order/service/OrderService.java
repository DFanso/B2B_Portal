package com.B2B.Portal.order.service;

import com.B2B.Portal.order.dto.OrderDTO;
import com.B2B.Portal.order.dto.ProductDTO;
import com.B2B.Portal.order.exception.InvalidProductException;
import com.B2B.Portal.order.exception.OrderNotFoundException;
import com.B2B.Portal.order.exception.UserNotFoundException;
import com.B2B.Portal.order.model.Order;
import com.B2B.Portal.order.repository.OrderRepository;
import com.B2B.Portal.product.exception.InvalidSupplierException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper, RestTemplateBuilder restTemplateBuilder) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.modelMapper = modelMapper;
        configureModelMapper();
    }

    private void configureModelMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private boolean validateUserId(Long userId, String userType) {
        String validationUrl = "http://128.199.128.10:8081/api/v1/users/" + userId;
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    validationUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            Map<String, Object> userMap = response.getBody();

            if (userMap != null) {
                String type = (String) userMap.get("type");
                return userType.equalsIgnoreCase(type);
            }
            return false;
        } catch (HttpClientErrorException e) {

            return false;
        }
    }

    private boolean validateProduct(Long productId, Long supplierId) {
        String validationUrl = "http://128.199.128.10:8082/api/v1/products/" + productId;
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    validationUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            Map<String, Object> productData = response.getBody();
            if (productData != null) {
                // Verify that the product is not null, is available (e.g., not DELETED or DISABLED),
                // and the supplier ID matches.
                Long fetchedSupplierId = ((Number) productData.get("supplierId")).longValue();
                return fetchedSupplierId.equals(supplierId);
            }
            return false;
        } catch (HttpClientErrorException e) {
            // Log error and/or handle it according to your application's needs
            return false;
        }
    }




    public OrderDTO createOrder(OrderDTO orderDTO) {

        // Validate customer ID
        if (!validateUserId(orderDTO.getCustomerId(), "CUSTOMER")) {
            throw new UserNotFoundException("Invalid customer ID");
        }

        Order order = modelMapper.map(orderDTO, Order.class);

        for (OrderDTO.OrderItemDTO item : orderDTO.getItems()) {
            if (!validateUserId(item.getSupplierId(), "SUPPLIER")) {
                throw new InvalidSupplierException("No valid supplier found with this product ID "+ item.getProductId());
            }
            if (!validateProduct(item.getProductId(), item.getSupplierId())) {
                throw new InvalidProductException("Invalid product ID " + item.getProductId() + " for supplier ID " + item.getSupplierId());
            }
        }

        // Set the order reference on each order item
        if (order.getItems() != null) {
            for (Order.OrderItem item : order.getItems()) {
                item.setOrder(order);
            }
        }

        Order savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        modelMapper.map(orderDTO, existingOrder);
        Order updatedOrder = orderRepository.save(existingOrder);
        return modelMapper.map(updatedOrder, OrderDTO.class);
    }

    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
        order.setStatus("cancelled");
        Order cancelledOrder = orderRepository.save(order);
        return modelMapper.map(cancelledOrder, OrderDTO.class);
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersBySupplierId(Long supplierId) {

        List<Order> orders = orderRepository.findBySupplierId(supplierId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

}
