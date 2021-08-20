package com.kyq.multids.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableJpaRepositories(entityManagerFactoryRef = "slaveEntityManagerFactory",
        transactionManagerRef = "jtaTransactionManager",
        basePackages = {"com.kyq.multids.modules.slave"})
public class SlaveDataSourceConfig {

    @Value("${spring.jta.enabled: false}")
    private boolean jta;

    /**
     * 数据源改为AtomikosDataSourceBean创建而不是DataSourceBuilder，否则jdbcTemplate操作数据库无法正常回滚。
     */
    @Bean("slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource(){
        return new AtomikosDataSourceBean();
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

    @Bean
    public JdbcTemplate slaveJdbcTemplate(@Qualifier("slaveDataSource")DataSource slaveDataSource){
        return new JdbcTemplate(slaveDataSource);
    }

    //使用jta事务管理器，不需要再单独为每个数据源配置PlatformTransactionManager
//    @Bean("slaveTransactionManager")
//    public PlatformTransactionManager slaveTransactionManager(@Qualifier("slaveEntityManagerFactory")EntityManagerFactory entityManagerFactory){
//        return new JpaTransactionManager(entityManagerFactory);
//    }
}
