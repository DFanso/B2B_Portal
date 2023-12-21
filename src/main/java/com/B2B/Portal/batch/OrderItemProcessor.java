package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;
import org.springframework.batch.item.ItemProcessor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderItemProcessor implements ItemProcessor<OrderDTO, Map<Long, List<OrderDTO.OrderItemDTO>>> {

    @Override
    public Map<Long, List<OrderDTO.OrderItemDTO>> process(OrderDTO order) {
        LocalDate today = LocalDate.now();

        if (!order.getOrderDate().toLocalDate().equals(today)) {
            return null;
        }

        Map<Long, List<OrderDTO.OrderItemDTO>> supplierOrdersMap = new HashMap<>();

        for (OrderDTO.OrderItemDTO item : order.getItems()) {
            Long supplierId = item.getSupplierId();
            supplierOrdersMap.computeIfAbsent(supplierId, k -> new ArrayList<>()).add(item);
        }

        return supplierOrdersMap;
    }
}
