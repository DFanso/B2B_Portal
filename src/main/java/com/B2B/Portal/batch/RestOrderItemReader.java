package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

public class RestOrderItemReader implements ItemReader<OrderDTO> {

    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextOrderIndex;
    private List<OrderDTO> orderData;

    public RestOrderItemReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.nextOrderIndex = 0;
    }

    @Override
    public OrderDTO read() throws Exception {
        if (orderDataIsNotInitialized()) {
            orderData = fetchOrderDataFromAPI();
        }

        OrderDTO nextOrder = null;

        if (nextOrderIndex < orderData.size()) {
            nextOrder = orderData.get(nextOrderIndex);
            nextOrderIndex++;
        }

        return nextOrder;
    }

    private boolean orderDataIsNotInitialized() {
        return this.orderData == null;
    }

    private List<OrderDTO> fetchOrderDataFromAPI() {
        OrderDTO[] orderArray = restTemplate.getForObject(apiUrl, OrderDTO[].class);
        assert orderArray != null;
        return Arrays.asList(orderArray);
    }
}
