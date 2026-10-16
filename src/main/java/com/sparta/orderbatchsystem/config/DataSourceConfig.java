package com.sparta.orderbatchsystem.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // 정산 DB
    @Primary
    @Bean(name = "settlementDataSource")
    @ConfigurationProperties("spring.datasource.settlement")
    public DataSource settlementDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 커머스 DB
    @Bean(name = "commerceDataSource")
    @ConfigurationProperties("spring.datasource.commerce")
    public DataSource commerceDataSource() {
        return DataSourceBuilder.create().build();
    }

    // Spring Batch가 사용할 DataSource 명시
    @Bean
    public BatchDataSourceScriptDatabaseInitializer batchDataSourceInitializer(
            @Qualifier("settlementDataSource") DataSource dataSource,
            BatchProperties properties) {
        return new BatchDataSourceScriptDatabaseInitializer(dataSource, properties.getJdbc());
    }

}
