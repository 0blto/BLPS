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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@DependsOn("transactionManager")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.drainshawty.lab1.repo.userdb",
        entityManagerFactoryRef = "userEntityManager",
        transactionManagerRef = "transactionManager"
)
public class UserDbConfig {

    @Autowired
    DataBuilder dataBuilder;

    @Bean(name = "userDataSource")
    public AtomikosDataSourceBean userDataSource() {
        return dataBuilder.buildDataSource("userdb");
    }

    @Bean(name = "userEntityManager")
    public LocalContainerEntityManagerFactoryBean userEntityManager(
            @Qualifier("userDataSource") DataSource dataSource) {
        return dataBuilder.buildEntityManager(dataSource,
                "com.drainshawty.lab1.model.userdb");
    }

}
