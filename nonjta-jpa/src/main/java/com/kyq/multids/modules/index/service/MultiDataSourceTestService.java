package com.kyq.multids.modules.index.service;

import java.util.Map;

/**
 * Description： com.kyq.multids.modules.index.service
 * CopyRight:  © 2015 CSTC. All rights reserved.
 * Company: cstc
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 14:27
 */
public interface MultiDataSourceTestService {
    /**
     * 跨数据源修改数据
     */
    void modifyDataAcrossDs();

    /**
     * 跨数据源使用原生SQL查询数据
     * @return
     */
    Map<String, Object> findDataAcrossDs();

    /**
     * 跨数据源使用jpa查询数据
     * @return
     */
    Map<String, Object> findDataAcrossDsByJpa();

    /**
     * 数据库保存报错，测试事务回滚
     */
    void errorModifyDataAcrossDs();

    /**
     * 直接在整合service里面使用repository
     */
    void addDict();

    /**
     * 直接在整合service里面使用repository
     */
    void addDictByError();

    /**
     * test
     */
    void errorModifyDataAcrossDsDirect();

    /**
     * test
     */
    void addByTemplate();
}
