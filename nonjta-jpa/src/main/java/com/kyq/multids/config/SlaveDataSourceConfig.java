package com.kyq.multids.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Description： com.kyq.multids.config
 * CopyRight:  © 2015 CSTC. All rights reserved.
 * Company: cstc
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 10:02
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "slaveEntityManagerFactory",
        transactionManagerRef = "slaveTransactionManager",
        basePackages = {"com.kyq.multids.modules.slave"})
public class SlaveDataSourceConfig {

    @Value("${spring.jta.enabled: false}")
    private boolean jta;

    @Bean("slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("slaveEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean slaveFactoryBean(@Qualifier("slaveDataSource") DataSource dataSource,
                                                                  @Qualifier("mysqlJpaProperties") JpaProperties jpaProperties){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        jpaVendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        jpaVendorAdapter.setShowSql(jpaProperties.isShowSql());
        jpaVendorAdapter.setDatabase(jpaProperties.getDatabase());

        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null)
                .dataSource(dataSource)
                .jta(jta)
                .persistenceUnit("slaveEntityManagerFactory")
                .packages("com.kyq.multids.modules.slave")
                .build();
    }

    @Bean("slaveTransactionManager")
    public PlatformTransactionManager slaveTransactionManager(@Qualifier("slaveEntityManagerFactory")EntityManagerFactory entityManagerFactory){
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public JdbcTemplate slaveJdbcTemplate(@Qualifier("slaveDataSource")DataSource slaveDataSource){
        return new JdbcTemplate(slaveDataSource);
    }
}
