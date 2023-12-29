package com.B2B.Portal.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.B2B.Portal.batch.dto.OrderDTO;

public class SupplierOrderCsvItemWriter implements ItemWriter<Map<Long, SupplierOrder>> {

    private static final Logger LOGGER = Logger.getLogger(SupplierOrderCsvItemWriter.class.getName());
    private static final String DIRECTORY_PATH = "/Users/dfanso/Programming/GitHub/B2B_Portal/";

    private void writeSupplierOrdersToCsv(Long supplierId, OrderDTO orderDTO, List<OrderDTO.OrderItemDTO> orderItems) {
        String fileName = DIRECTORY_PATH + "supplier_" + supplierId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".csv";
        boolean isNewFile = !new File(fileName).exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            // Write the header line only if it's a new file
            if (isNewFile) {
                writer.println("OrderID,SUPPLIER_ID,SUPPLIER_NAME,PRODUCT_ID,PRODUCT_DESCRIPTION,QUANTITY,CUSTOMER_ID,CUSTOMER_NAME,DELIVERY_ADDRESS,DELIVERY_DATE");
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (OrderDTO.OrderItemDTO itemDTO : orderItems) {
                String deliveryDate = (orderDTO.getDeliveryDate() != null) ? orderDTO.getDeliveryDate().format(dateFormatter) : "";

                // Write details for each OrderItemDTO and some information from OrderDTO
                writer.println(orderDTO.getOrderId() + "," + itemDTO.getSupplierId() + "," + escapeCsv(itemDTO.getSupplierName()) +
                        "," + itemDTO.getProductId() + "," + escapeCsv(itemDTO.getProductDescription()) + "," + itemDTO.getQuantity() +
                        "," + orderDTO.getCustomerId() + "," + escapeCsv(orderDTO.getCustomerName()) + "," + escapeCsv(orderDTO.getDeliveryAddress()) +
                        "," + deliveryDate);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file: " + fileName, e);
        }
    }

    private String escapeCsv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
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
