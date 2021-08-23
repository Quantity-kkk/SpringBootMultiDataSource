package com.kyq.multids.modules.slave.dict.service.impl;

import com.kyq.multids.modules.slave.dict.domain.BaseDict;
import com.kyq.multids.modules.slave.dict.repository.DictRepository;
import com.kyq.multids.modules.slave.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Description： com.kyq.multids.modules.slave.dict.service.impl
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-19 17:03
 */
@Service
@Transactional(value = "slaveTransactionManager", rollbackFor = RuntimeException.class)
public class DictServiceImpl implements DictService {

    @Autowired
    DictRepository dictRepository;

    @Autowired
    @Qualifier("slaveJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public void addDict() {
        BaseDict dict = new BaseDict();
        dict.setPid("*");
        dict.setCodeType("sys_code");
        dict.setBaseCode("1");
        dict.setCodeDesc("系统编码");
        dictRepository.save(dict);
    }

    public void addDictByError() {
        BaseDict dict = new BaseDict();
        dict.setPid("*");
        dict.setCodeType("sys_code");
        dict.setBaseCode("2");
        dict.setCodeDesc("用户编码");
        dictRepository.save(dict);
        //正常情况下，dict不应该保存成功
        throw new RuntimeException("保存报错，测试事务回滚");
    }

    public void addDictByJdbcOnError(){
        jdbcTemplate.update("insert into base_dict (base_code, code_desc, code_type,  pid, id) values (?, ?, ?, ?, ?)",
                "2", "用户编码", "sys_code", "*", UUID.randomUUID().toString().replaceAll("-",""));
        throw new RuntimeException("JdbcTemplate添加出错");
    }
}
