package com.kyq.multids.modules.index.service.impl;

import com.kyq.multids.modules.index.service.MultiDataSourceTestService;
import com.kyq.multids.modules.main.user.repository.UserRepository;
import com.kyq.multids.modules.main.user.service.UserService;
import com.kyq.multids.modules.slave.dict.repository.DictRepository;
import com.kyq.multids.modules.slave.dict.service.DictService;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description： com.kyq.multids.modules.index.service.impl
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 14:27
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
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
     * 测试跨数据库保存数据
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

    public Map<String, Object> findDataAcrossDsByJpa(){
         Map<String, Object> ret = new HashMap<String, Object>(4);

        ret.put("user", userRepository.findAll());
        ret.put("dict", dictRepository.findAll());
         return ret;
    }


    /**
     * 测试跨数据库保存数据
     * 此案例使用JtaTransactionManager统一管理事务，user和dict都不会保存，回滚正常。
     *
     * */
    public void errorModifyDataAcrossDs() {
        userService.addUser();
        dictService.addDictByError();
    }

    public void addDict(){
        dictService.addDict();
    }

    /**
     * 测试跨数据库保存数据
     * 使用jdbcTemplate进行数据库修改操作，user和dict都不会保存，回滚正常。
     * 触发了两次txInfo，分别是addDictByJdbcOnError和addByTemplate
     * */
    public void addByTemplate(){
        userService.addUserByJdbc();
        dictService.addDictByJdbcOnError();
    }
}
