package com.smartviet.base.cab.database;

import com.smartviet.base.salary.common.Constants;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = Constants.Database.Primary.PACKAGE_REPO)
@EntityScan(basePackages = Constants.Database.Primary.PACKAGE_ENTITY)
public class DataSourcePrimaryConfig {

    @Primary
    @Bean(name = Constants.Database.Primary.BEAN_PRIMARY_DATASOURCE)
    @ConfigurationProperties(prefix = Constants.Database.Primary.PROPERTY_PREFIX)
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = Constants.Database.Primary.BEAN_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            @Qualifier(Constants.Database.Primary.BEAN_PRIMARY_DATASOURCE) DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages(Constants.Database.Primary.PACKAGE_ENTITY)
                .persistenceUnit(Constants.Database.Primary.UNIT)
                .build();
    }

    @Primary
    @Bean(name = Constants.Database.Primary.BEAN_TRANSACTION_MANAGER)
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier(Constants.Database.Primary.BEAN_ENTITY_MANAGER_FACTORY) EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
