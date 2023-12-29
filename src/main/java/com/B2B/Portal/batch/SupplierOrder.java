package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;

import java.util.List;

public class SupplierOrder {
    private final OrderDTO order;
    private final List<OrderDTO.OrderItemDTO> items;

    public SupplierOrder(OrderDTO order, List<OrderDTO.OrderItemDTO> items) {
        this.order = order;
        this.items = items;
    }

    public OrderDTO getOrder() {
        return order;
    }

    public List<OrderDTO.OrderItemDTO> getItems() {
        return items;
    }
}

