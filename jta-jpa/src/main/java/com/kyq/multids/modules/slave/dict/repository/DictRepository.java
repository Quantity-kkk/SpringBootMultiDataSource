package com.kyq.multids.modules.slave.dict.repository;

import com.kyq.multids.modules.slave.dict.domain.BaseDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description： com.kyq.multids.modules.slave.dict.repository
 * CopyRight:  © 2015 CSTC. All rights reserved.
 * Company: cstc
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 14:25
 */
@Repository
public interface DictRepository extends JpaRepository<BaseDict, String> {
}
