package com.B2B.Portal.order.controller;

import com.B2B.Portal.order.dto.OrderDTO;
import com.B2B.Portal.order.exception.InvalidProductException;
import com.B2B.Portal.order.exception.UserNotFoundException;
import com.B2B.Portal.order.service.OrderService;
import com.B2B.Portal.product.exception.InvalidSupplierException;
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
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderDTO.setStatus("PENDING");
            OrderDTO newOrder = orderService.createOrder(orderDTO);
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (UserNotFoundException | InvalidSupplierException | InvalidProductException ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while creating the order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
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
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable String customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<OrderDTO>> getOrdersBySupplierId(@PathVariable Long supplierId) {
        List<OrderDTO> orders = orderService.getOrdersBySupplierId(supplierId);
        return ResponseEntity.ok(orders);
    }
}
