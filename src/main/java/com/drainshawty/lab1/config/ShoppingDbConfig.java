package com.drainshawty.lab1.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.drainshawty.lab1.repo.shoppingdb",
        entityManagerFactoryRef = "shoppingEntityManager",
        transactionManagerRef = "transactionManager"
)
public class ShoppingDbConfig {

    @Autowired
    DataBuilder dataBuilder;

    @Bean(name = "shoppingDataSource")
    public AtomikosDataSourceBean shoppingDataSource() {
        return dataBuilder.buildDataSource("shoppingdb");
    }


    @Bean(name = "shoppingEntityManager")
    public EntityManagerFactory shoppingEntityManager(
            @Qualifier("shoppingDataSource") DataSource dataSource) {
        return dataBuilder.buildEntityManager(dataSource,
                "com.drainshawty.lab1.model.shoppingdb");
    }


}
