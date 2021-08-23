package com.kyq.multids.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 10:02
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "mainEntityManagerFactory",
        transactionManagerRef = "mainTransactionManager",
//        此处的packages用于repository扫描管理
        basePackages = {"com.kyq.multids.modules.main"})
public class MainDataSourceConfig {
    @Value("${spring.jta.enabled: false}")
    private boolean jta;

    @Primary
    @Bean("mysqlJpaProperties")
    @ConfigurationProperties(prefix = "spring.jpa.mysql")
    public JpaProperties mysqlJpaProperties() {
        return new JpaProperties();
    }


    @Primary
    @Bean("mainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.main")
    public DataSource mainDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean("mainEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mainFactoryBean(@Qualifier("mainDataSource") DataSource dataSource,
                                                                  @Qualifier("mysqlJpaProperties") JpaProperties jpaProperties){
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        jpaVendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        jpaVendorAdapter.setShowSql(jpaProperties.isShowSql());
        jpaVendorAdapter.setDatabase(jpaProperties.getDatabase());

        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null)
                .dataSource(dataSource)
                .jta(jta)
                .persistenceUnit("mainEntityManagerFactory")
//                此处的packages用于domain扫描管理
                .packages("com.kyq.multids.modules.main")
                .build();
    }

    @Primary
    @Bean
    public JdbcTemplate mainJdbcTemplate(@Qualifier("mainDataSource") DataSource mainDataSource){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(mainDataSource);
        return jdbcTemplate;
    }

    @Primary
    @Bean("mainTransactionManager")
    public PlatformTransactionManager mainTransactionManager(@Qualifier("mainEntityManagerFactory") EntityManagerFactory entityManagerFactory){

        return new JpaTransactionManager(entityManagerFactory);
    }

}
