package com.kyq.multids.modules.index.service.impl;

import com.kyq.multids.modules.index.service.MultiDataSourceTestService;
import com.kyq.multids.modules.main.user.domain.BaseUser;
import com.kyq.multids.modules.main.user.repository.UserRepository;
import com.kyq.multids.modules.main.user.service.UserService;
import com.kyq.multids.modules.slave.dict.domain.BaseDict;
import com.kyq.multids.modules.slave.dict.repository.DictRepository;
import com.kyq.multids.modules.slave.dict.service.DictService;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description： com.kyq.multids.modules.index.service.impl
 * 注意，因为本项目有着多个transactionManager各自管理各自的数据库，
 *      nonjta项目的MultiDataSourceTestServiceImpl是没有在class上添加@Transactional的。
 *
 * CopyRight:  © 2015 CSTC. All rights reserved.
 * Company: cstc
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 14:27
 */
@Service
public class MultiDataSourceTestServiceImpl implements MultiDataSourceTestService {

    @Autowired
    UserService userService;

    @Autowired
    DictService dictService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    DictRepository dictRepository;

    /**
     * 此处Qualifier注解指向的是一个beanFactory对象而不是一个entityManger对象，对此有兴趣可参见我的博客对此有详细说明。
     * */
    @Autowired
    @Qualifier("mainEntityManagerFactory")
    EntityManager mainEntityManager;

    @PersistenceContext(unitName = "slaveEntityManagerFactory")
    EntityManager slaveEntityManager;

    /**
     * 测试正常跨数据库保存数据
     * */
    public void modifyDataAcrossDs() {
        userService.addUser();

        dictService.addDict();
    }

    /**
     * 测试使用原生SQL语句跨数据库查询数据
     *
     * @return*/
    public Map<String, Object> findDataAcrossDs(){
        Map<String, Object> ret = new HashMap<String, Object>(4);

        Query userQuery = mainEntityManager.createNativeQuery("select * from base_user")
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List userList = userQuery.getResultList();


        Query dictQuery = slaveEntityManager.createNativeQuery("select * from base_dict")
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List dictList = dictQuery.getResultList();

        ret.put("user", userList);
        ret.put("dict", dictList);
        return ret;
    }

    /**
     * 测试使用JPA方式跨数据库查询数据
     *
     * @return*/
    public Map<String, Object> findDataAcrossDsByJpa(){
         Map<String, Object> ret = new HashMap<String, Object>(4);

        ret.put("user", userRepository.findAll());
        ret.put("dict", dictRepository.findAll());
         return ret;
    }


    /**
     * 测试使用JPA方式跨数据库保存数据
     * 配置：user使用独立的service，添加@Transactional注解，指明mainTransactionManager
     *      dict使用独立的service, 添加@Transactional注解，指明slaveTransactionManager
     *
     * 结果：未使用分布式事务的情况，user保存成功，但是dict保存失败并回滚。符合预期
     *
     * */
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRES_NEW)
    public void errorModifyDataAcrossDs() {
        userService.addUser();
        dictService.addDictByError();
    }

    /**
     * @Description 测试使用JPA方式跨数据库保存数据，验证@Transactional的正确使用方式
     * case1：errorModifyDataAcrossDsDirect不添加@Transactional注解，在addUser和addDict方法中添加注解，不会触发事务回滚。
     * case2: errorModifyDataAcrossDsDirect添加@Transactional注解，不指定transactionManager，触发事务回滚，使用默认的mainTransactionManager，
     *        user添加失败，dict添加成功。
     * case3: errorModifyDataAcrossDsDirect添加@Transactional注解，指定transactionManager为slaveTransactionManager，触发事务回滚，
     *        user添加成功，dict添加失败。
     *
     *  @总结： 多数据源环境下，因为spring事务基于aop实现，@Transactional在同一个类方法中使用时，可能产生意想不到的情况导致失效，
     *          且事务管理器切换也无法正常切换，建议多数据源情况下分别在不同service中使用事务，并通过新的service注入整合。
     * */
//    @Transactional(rollbackFor = RuntimeException.class)
//    @Transactional(transactionManager = "slaveTransactionManager" , rollbackFor = RuntimeException.class)
    public void errorModifyDataAcrossDsDirect() {
        this.addUser();
        this.addDictByError();
    }

    @Transactional(transactionManager = "mainTransactionManager", rollbackFor = RuntimeException.class)
    public void addUser(){
        BaseUser user = new BaseUser();
        user.setUserId("zhangsan");
        user.setUserName("张三");
        userRepository.save(user);
    }

    @Transactional(transactionManager = "slaveTransactionManager", rollbackFor = RuntimeException.class)
    public void addDict(){
        BaseDict dict = new BaseDict();
        dict.setPid("*");
        dict.setCodeType("sys_code");
        dict.setBaseCode("1");
        dict.setCodeDesc("系统编码");
        dictRepository.save(dict);
    }

    @Transactional(transactionManager = "slaveTransactionManager", rollbackFor = RuntimeException.class)
    public void addDictByError(){
        BaseDict dict = new BaseDict();
        dict.setPid("*");
        dict.setCodeType("sys_code");
        dict.setBaseCode("1");
        dict.setCodeDesc("系统编码");
        dictRepository.save(dict);
        //正常情况下，dict不应该保存成功
        throw new RuntimeException("保存报错，测试事务回滚");
    }

    /**
     * 测试跨数据库保存数据
     * case1: 使用jdbcTemplate进行数据库修改操作，指定txManager为transactionManager时，user和dict都不会保存，回滚正常；
     * case2: 指定txManager为slaveTransactionManager时，user会保存成功；
     *
     * 触发了两次txInfo，分别是addDictByJdbcOnError和addByTemplate
     * */
//    @Transactional(rollbackFor = RuntimeException.class)
    @Transactional(transactionManager = "slaveTransactionManager", rollbackFor = RuntimeException.class)
    public void addByTemplate(){
        userService.addUserByJdbc();
        dictService.addDictByJdbcOnError();
    }
}
