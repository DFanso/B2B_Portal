package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupplierOrderCsvItemWriter implements ItemWriter<Map<Long, List<OrderDTO.OrderItemDTO>>> {

    private static final Logger LOGGER = Logger.getLogger(SupplierOrderCsvItemWriter.class.getName());
    private static final String DIRECTORY_PATH = "/Users/dfanso/Programming/GitHub/B2B_Portal/"; // Define your directory path here

    private void writeSupplierOrdersToCsv(Long supplierId, List<OrderDTO.OrderItemDTO> items) {
        String fileName = DIRECTORY_PATH + "supplier_" + supplierId +"_" + LocalDate.now() +".csv";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            for (OrderDTO.OrderItemDTO item : items) {
                writer.println(item.getProductId() + "," + item.getQuantity() + "," + item.getPrice());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + fileName, e);
        }
    }


    @Override
    public void write(Chunk<? extends Map<Long, List<OrderDTO.OrderItemDTO>>> chunk) throws Exception {
        for (Map<Long, List<OrderDTO.OrderItemDTO>> itemMap : chunk) {
            for (Map.Entry<Long, List<OrderDTO.OrderItemDTO>> entry : itemMap.entrySet()) {
                writeSupplierOrdersToCsv(entry.getKey(), entry.getValue());
            }
        }

    }
}
