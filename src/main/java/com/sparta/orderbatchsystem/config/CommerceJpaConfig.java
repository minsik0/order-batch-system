package com.sparta.orderbatchsystem.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class CommerceJpaConfig {

    @Bean(name = "commerceEmf")
    public LocalContainerEntityManagerFactoryBean commerceEntityManagerFactory(
            @Qualifier("commerceDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.orderbatch.domain.commerce");
        emf.setJpaVendorAdapter(vendorAdapter);
        emf.setJpaPropertyMap(jpaProperties.getProperties());
        return emf;
    }

    @Bean(name = "commerceTransactionManager")
    public PlatformTransactionManager commerceTransactionManager(
            @Qualifier("commerceEmf") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
