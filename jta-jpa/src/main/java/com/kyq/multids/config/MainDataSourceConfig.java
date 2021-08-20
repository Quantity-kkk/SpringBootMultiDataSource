package com.kyq.multids.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

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
@EnableJpaRepositories(entityManagerFactoryRef = "mainEntityManagerFactory",
        transactionManagerRef = "jtaTransactionManager",
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

    /**
     * 数据源改为AtomikosDataSourceBean创建而不是DataSourceBuilder，否则jdbcTemplate操作数据库无法正常回滚。
     */
    @Primary
    @Bean("mainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.main")
    public DataSource mainDataSource(){
        return new AtomikosDataSourceBean();
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
                .packages("com.kyq.multids.modules.main")
                .build();
    }

    @Primary
    @Bean
    public JdbcTemplate mainJdbcTemplate(@Qualifier("mainDataSource") DataSource mainDataSource){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(mainDataSource);
        return jdbcTemplate;
    }

    //使用jta事务管理器，不需要再单独为每个数据源配置PlatformTransactionManager
//    @Primary
//    @Bean("mainTransactionManager")
//    public PlatformTransactionManager mainTransactionManager(@Qualifier("mainEntityManagerFactory") EntityManagerFactory entityManagerFactory){
//        return new JpaTransactionManager(entityManagerFactory);
//    }

}
