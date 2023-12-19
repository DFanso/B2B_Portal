package com.B2B.Portal.order.controller;

import com.B2B.Portal.order.dto.OrderDTO;
import com.B2B.Portal.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO newOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(orderId, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        OrderDTO cancelledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(cancelledOrder);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<OrderDTO>> getOrdersBySupplierId(@PathVariable Long supplierId) {
        List<OrderDTO> orders = orderService.getOrdersBySupplierId(supplierId);
        return ResponseEntity.ok(orders);
    }

    // Any additional endpoints as per your API spec can be added here
}
