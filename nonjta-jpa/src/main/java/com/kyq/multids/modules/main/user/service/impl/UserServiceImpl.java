package com.kyq.multids.modules.main.user.service.impl;

import com.kyq.multids.modules.main.user.domain.BaseUser;
import com.kyq.multids.modules.main.user.repository.UserRepository;
import com.kyq.multids.modules.main.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Description： com.kyq.multids.modules.main.user.service.impl
 * CopyRight:  © 2015 CSTC. All rights reserved.
 * Company: cstc
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-19 17:02
 */
@Service
@Transactional(value = "mainTransactionManager", rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    @Qualifier("mainJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public void addUser() {
        BaseUser user = new BaseUser();
        user.setUserId("lisi");
        user.setUserName("李四");
        userRepository.save(user);
    }

    public void addUserByJdbc(){
        jdbcTemplate.update("insert into base_user (user_id, user_name, id) values (?, ?, ?)",
                "lisi", "李四", UUID.randomUUID().toString().replaceAll("-",""));
    }
}
