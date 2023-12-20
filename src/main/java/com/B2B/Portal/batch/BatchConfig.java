package com.B2B.Portal.batch;

import com.B2B.Portal.batch.RestOrderItemReader;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;
import com.B2B.Portal.batch.dto.OrderDTO;

import java.util.List;
import java.util.Map;

@Configuration
public class BatchConfig {

    private final String apiUrl = "http://localhost:8082/api/v1/orders";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

//    @Scheduled(cron = "0 * * * * *") // Run every minute
//    public void runBatchJob() {
//        try {
//            // Create JobParameters with a unique identifier (e.g., timestamp)
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addLong("time", System.currentTimeMillis())
//                    .toJobParameters();
//
//            // Launch the job with the provided JobParameters
//            JobExecution jobExecution = jobLauncher.run(orderStep, jobParameters);
//
//            // Handle job execution status if needed
//            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
//                // Job completed successfully
//            } else {
//                // Handle job failure or other status
//            }
//        } catch (Exception e) {
//            // Handle exceptions if the job launch fails
//        }
//    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ItemReader<OrderDTO> orderItemReader() {
        return new RestOrderItemReader(apiUrl, restTemplate());
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
    public Step orderStep(
            ItemReader<OrderDTO> orderItemReader,
            OrderItemProcessor orderItemProcessor,
            SupplierOrderCsvItemWriter supplierOrderCsvItemWriter) {
        return new StepBuilder("orderStep", jobRepository)
                .<OrderDTO, Map<Long, List<OrderDTO.OrderItemDTO>>>chunk(10)
                .reader(orderItemReader)
                .processor(orderItemProcessor)
                .writer(supplierOrderCsvItemWriter)
                .transactionManager(transactionManager)
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
