package com.kyq.multids.modules.slave.dict.service;

/**
 * Description： com.kyq.multids.modules.slave.dict.service
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-19 17:00
 */
public interface DictService {

    /**
     * 正常保存字典
     */
    void addDict();

    /**
     * 添加字典主动抛出错误
     */
    void addDictByError();

    /**
     * 测试jdbcTemplate的事务
     */
    void addDictByJdbcOnError();
}
