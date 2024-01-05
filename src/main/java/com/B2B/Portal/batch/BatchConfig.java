package com.B2B.Portal.batch;

import com.B2B.Portal.batch.dto.OrderDTO;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
@EnableScheduling
public class BatchConfig {

    private final String apiUrl = "http://localhost:8082/api/v1/orders";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }



    @Bean
    public ItemReader<OrderDTO> orderItemReader() {
        return new RestOrderItemReader(apiUrl, new RestTemplate());
    }

    @Bean
    public SupplierOrderCsvItemWriter supplierOrderCsvItemWriter() {
        return new SupplierOrderCsvItemWriter();
    }
    @Bean
    public OrderItemProcessor orderItemProcessor() {
        return new OrderItemProcessor();
    }

    @Bean
    public Step orderStep() {
        return new StepBuilder("orderStep", jobRepository)
                .<OrderDTO, Map<Long, SupplierOrder>>chunk(10, transactionManager)  // Update the generic types here
                .reader(orderItemReader())
                .processor(orderItemProcessor())
                .writer(supplierOrderCsvItemWriter())
                .build();
    }




    @Bean
    public Job orderJob(Step orderStep) {
        return new JobBuilder("orderJob", jobRepository)
                .start(orderStep)
                .build();
    }





    // Other batch configuration (job, step, etc.)
}
