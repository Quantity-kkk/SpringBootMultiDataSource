package com.kyq.multids.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * Description： com.kyq.multids.config
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 15:36
 */
@Configuration
@EnableTransactionManagement
public class JtaTransactionManagerConfig {

    @Primary
    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

    @Primary
    @Bean(name = "userTransactionManager")
    public TransactionManager atomikosTransactionManager(){
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }


    @Primary
    @Bean(name = "jtaTransactionManager")
    public PlatformTransactionManager jtaTransactionManager (@Qualifier("userTransaction") UserTransaction userTransaction,
                                                             @Qualifier("userTransactionManager") TransactionManager userTransactionManager) {
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }
}
