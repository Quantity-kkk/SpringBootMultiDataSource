package com.kyq.multids.modules.main.user.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Description： com.kyq.multids.modules.main.user.domain
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 13:37
 */
@Data
@ToString
@Entity
@Table(name = "base_user")
public class BaseUser {
    @Id
    @Column(name = "id",length = 32,unique = true, nullable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "user_id" , length = 64)
    private String userId;

    @Column(name = "user_name" , length = 256)
    private String userName;

    @CreatedBy
    @Column(name = "CREATE_BY", length = 64)
    private String createBy;

    @CreatedDate
    @Column(name = "CREATE_TIME")
    private Date createTime;

    @LastModifiedBy
    @Column(name = "UPDATE_BY", length = 64)
    private String updateBy;

    @LastModifiedDate
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
}
