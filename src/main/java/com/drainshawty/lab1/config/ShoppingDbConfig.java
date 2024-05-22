package com.drainshawty.lab1.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.drainshawty.lab1.repo.shoppingdb",
        entityManagerFactoryRef = "shoppingEntityManagerFactory",
        transactionManagerRef = "shoppingTransactionManager"
)
public class ShoppingDbConfig {

    @Bean(name = "shoppingDataSource")
    @ConfigurationProperties(prefix = "spring.shoppingdb")
    public DataSource shoppingDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "shoppingEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean shoppingEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("shoppingDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.drainshawty.lab1.model.shoppingdb")
                .build();
    }


    @Bean(name = "shoppingTransactionManager")
    public PlatformTransactionManager shoppingTransactionManager(
            @Qualifier("shoppingEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
