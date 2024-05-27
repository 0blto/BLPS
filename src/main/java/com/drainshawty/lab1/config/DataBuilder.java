package com.drainshawty.lab1.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@Component
public class DataBuilder {

    @Autowired
    private Environment env;

    String getParameter(String prefix, String name) {
        return "spring." + prefix + "." + name;
    }

    public AtomikosDataSourceBean buildDataSource(String prefix) {
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setUniqueResourceName(prefix);
        xaDataSource.setXaDataSourceClassName(env.getProperty(getParameter(prefix, "driver-class-name")));
        xaDataSource.setXaDataSource(buildXaDataSource(prefix));
        xaDataSource.setBorrowConnectionTimeout(30);
        xaDataSource.setMaxPoolSize(50);
        return xaDataSource;
    }

    private PGXADataSource buildXaDataSource(String prefix) {
        PGXADataSource properties = new PGXADataSource();
        System.out.println(getParameter(prefix, "username"));
        properties.setUser(env.getProperty(getParameter(prefix, "username")));
        properties.setPassword(env.getProperty(getParameter(prefix, "password")));
        properties.setUrl(env.getProperty(getParameter(prefix, "jdbc-url")));
        return properties;
    }

    public EntityManagerFactory buildEntityManager(DataSource dataSource, String packageName) {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(packageName);
        factory.setJpaProperties(properties);
        return factory.getObject();
    }
}
