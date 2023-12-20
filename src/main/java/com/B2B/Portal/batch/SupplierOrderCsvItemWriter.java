package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SupplierOrderCsvItemWriter extends FlatFileItemWriter<Map<Long, List<OrderDTO>>> {

    public SupplierOrderCsvItemWriter() {
        String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        WritableResource resource = new FileSystemResource("supplier-orders-" + dateStamp + ".csv");
        setResource(resource);

        setLineAggregator(new LineAggregator<Map<Long, List<OrderDTO>>>() {
            @Override
            public String aggregate(Map<Long, List<OrderDTO>> supplierOrders) {
                StringBuilder lines = new StringBuilder();
                supplierOrders.forEach((supplierId, orders) -> {
                    orders.forEach(order -> {
                        order.getItems().forEach(item -> {
                            if (item.getSupplierId().equals(supplierId)) {
                                lines.append(String.format("%d,%d,%d,%f%n",
                                        order.getOrderId(),
                                        item.getProductId(),
                                        item.getQuantity(),
                                        item.getPrice()));
                            }
                        });
                    });
                });
                return lines.toString();
            }
        });
    }
}
