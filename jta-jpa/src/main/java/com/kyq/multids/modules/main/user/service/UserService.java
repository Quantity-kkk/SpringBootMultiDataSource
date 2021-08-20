package com.kyq.multids.modules.main.user.service;

/**
 * Description： com.kyq.multids.modules.main.user.service
 * CopyRight:  © 2015 CSTC. All rights reserved.
 * Company: cstc
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-19 16:59
 */
public interface UserService {
    /**
     * 正常添加用户
     * */
    void addUser();

    /**
     * 使用jdbcTemplate操作数据库
     */
    void addUserByJdbc();
}
