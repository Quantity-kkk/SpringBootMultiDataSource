package com.kyq.multids.modules.main.user.repository;

import com.kyq.multids.modules.main.user.domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description： com.kyq.multids.modules.main.user.repository
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 14:25
 */
@Repository
public interface UserRepository extends JpaRepository<BaseUser, String> {
}
