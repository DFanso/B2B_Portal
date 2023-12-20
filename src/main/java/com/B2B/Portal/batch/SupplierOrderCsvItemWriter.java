package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO.OrderItemDTO;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SupplierOrderCsvItemWriter extends FlatFileItemWriter<Map<Long, List<OrderItemDTO>>> {

    public SupplierOrderCsvItemWriter() {
        // Naming the file with a date stamp
        try {
            String dateStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String directoryPath = "/Users/dfanso/Programming/GitHub/B2B_Portal/";
            String filePath = directoryPath + "supplier-orders-" + dateStamp + ".csv";

            Path directory = Paths.get(directoryPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            WritableResource resource = new FileSystemResource(filePath);
            setResource(resource);

            // ... (rest of your code for setting line aggregator)
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file or directory", e);
        }

        // Defining how each line in the file will be aggregated from the domain object
        setLineAggregator(new LineAggregator<Map<Long, List<OrderItemDTO>>>() {
            @Override
            public String aggregate(Map<Long, List<OrderItemDTO>> supplierOrders) {
                StringBuilder lines = new StringBuilder();
                for (Map.Entry<Long, List<OrderItemDTO>> entry : supplierOrders.entrySet()) {
                    Long supplierId = entry.getKey();
                    List<OrderItemDTO> orderItems = entry.getValue();
                    for (OrderItemDTO item : orderItems) {
                        lines.append(String.format("%d,%d,%f%n",
                                //item.getOrderId(),  // Assuming OrderItemDTO has getOrderId()
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice()));
                    }
                }
                return lines.toString();
            }
        });
    }
}
