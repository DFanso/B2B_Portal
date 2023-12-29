package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;
import org.springframework.batch.item.ItemProcessor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderItemProcessor implements ItemProcessor<OrderDTO, Map<Long, SupplierOrder>> {

    @Override
    public Map<Long, SupplierOrder> process(OrderDTO order) {
        LocalDate today = LocalDate.now();

        if (!order.getOrderDate().toLocalDate().equals(today)) {
            return null;
        }

        Map<Long, SupplierOrder> supplierOrdersMap = new HashMap<>();

        for (OrderDTO.OrderItemDTO item : order.getItems()) {
            Long supplierId = item.getSupplierId();
            supplierOrdersMap.computeIfAbsent(supplierId, k -> new SupplierOrder(order, new ArrayList<>()))
                    .getItems().add(item);
        }

        return supplierOrdersMap;
    }
}


