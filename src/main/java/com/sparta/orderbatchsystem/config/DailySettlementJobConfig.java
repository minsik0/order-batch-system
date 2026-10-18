package com.sparta.orderbatchsystem.config;

import com.sparta.orderbatchsystem.domain.Order;
import com.sparta.orderbatchsystem.domain.Settlement;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DailySettlementJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory commerceEmf;  // 커머스 DB
    private final SettlementItemWriter settlementItemWriter;
    private final SettlementJobListener settlementJobListener;

    @Bean
    public Job dailySettlementJob() {
        return new JobBuilder("dailySettlementJob", jobRepository)
                .listener(settlementJobListener)
                .start(settlementStep())
                .next(summaryStep())
                .build();
    }

    @Bean
    public step settlementStep() {
        return new StepBuilder("settlementStep", jobRepository)
                .<Order, Settlement>chunk(1000, transactionManager)
                .reader(orderItemReader(null))
                .processor(settlementProcessor)
                .writer(settlementItemWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }

    @Bean
    public Step summaryStep() {
        return new StepBuilder("summaryStep", jobRepository)
                .tasklet(summaryTasklet(null), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Order> orderItemReader(
            @Value("#{jobParameters['settlementDate']}") LocalDate settlementDate) {

        LocalDateTime start = settlementDate.atStartOfDay();
        LocalDateTime end = settlementDate.plusDays(1).atStartOfDay();

        return new JpaPagingItemReaderBuilder<Order>()
                .name("orderItemReader")
                .entityManagerFactory(commerceEmf)
                .pageSize(1000)
                .queryString("""
                    SELECT o FROM Order o
                    WHERE o.status = 'COMPLETED'
                    AND o.completedAt >= :start
                    AND o.completedAt < :end
                    ORDER BY o.id ASC
                """)
                .parameterValues(Map.of("start", start, "end", end))
                .build();
    }

    @Bean
    public SettlementProcessor settlementProcessor() {
        return new SettlementProcessor();
    }

    @Bean
    @StepScope
    public Tasklet summaryTasklet(
            @Value("#{jobParameters['settlementDate']}") LocalDate settlementDate) {
        return (contribution, chunkContext) -> {
            // Step 4 구현 — 아래에서 설명
            return RepeatStatus.FINISHED;
        };
    }
}
