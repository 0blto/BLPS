package com.drainshawty.lab1.config;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

@Component
public class DataBuilder {

    @Autowired
    private Environment env;

    @Autowired
    private JpaVendorAdapter adapter;

    String getParameter(String prefix, String name) {
        return "spring." + prefix + "." + name;
    }

    public AtomikosDataSourceBean buildDataSource(String prefix) {
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setUniqueResourceName(prefix);
        xaDataSource.setXaDataSourceClassName(env.getProperty(getParameter(prefix, "driver-class-name")));
        xaDataSource.setXaDataSource(buildXaDataSource(prefix));
        xaDataSource.setBorrowConnectionTimeout(10);
        xaDataSource.setMaxPoolSize(10);
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

    public LocalContainerEntityManagerFactoryBean buildEntityManager(DataSource dataSource, String packageName) {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(adapter);
        factory.setJtaDataSource(dataSource);
        factory.setPackagesToScan(packageName);
        factory.setJpaProperties(properties);
        return factory;
    }
}
