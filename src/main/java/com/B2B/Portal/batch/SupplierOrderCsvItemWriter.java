package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupplierOrderCsvItemWriter implements ItemWriter<Map<Long, SupplierOrder>> {

    private static final Logger LOGGER = Logger.getLogger(SupplierOrderCsvItemWriter.class.getName());
    private static final String DIRECTORY_PATH = "/Users/dfanso/Programming/GitHub/B2B_Portal/"; // Define your directory path here

    private void writeSupplierOrdersToCsv(Long supplierId, OrderDTO orderDTO, List<OrderDTO.OrderItemDTO> orderItems) {
        String fileName = DIRECTORY_PATH + "supplier_" + supplierId + "_" + LocalDate.now() + ".csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            // Write the header line
            writer.println("Product_ID,Quantity,Price,Order_Date,Delivery_Date");

            for (OrderDTO.OrderItemDTO itemDTO : orderItems) {
                // Write details for each OrderItemDTO and some information from OrderDTO
                writer.println(itemDTO.getProductId() + "," + itemDTO.getQuantity() + "," + itemDTO.getPrice() +
                        "," + orderDTO.getOrderDate() + "," + orderDTO.getDeliveryDate());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + fileName, e);
        }
    }


    @Override
    public void write(Chunk<? extends Map<Long, SupplierOrder>> chunk) throws Exception {
        for (Map<Long, SupplierOrder> itemMap : chunk) {
            for (Map.Entry<Long, SupplierOrder> entry : itemMap.entrySet()) {
                Long supplierId = entry.getKey();
                SupplierOrder supplierOrder = entry.getValue();

                writeSupplierOrdersToCsv(supplierId, supplierOrder.getOrder(), supplierOrder.getItems());
            }
        }
    }
}
