package com.kyq.multids.modules.slave.dict.domain;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Description： com.kyq.multids.modules.slave.dict.domain
 *
 * @author kyq1024
 * @version 1.0
 * @timestamp 2021-08-18 13:46
 */
@Data
@ToString
@Entity
@Table(name = "base_dict",
        indexes = {@Index(name = "idx_dict_pid", columnList = "pid")})
public class BaseDict {
    @Id
    @Column(name = "id",length = 32,unique = true, nullable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "pid" , length = 32)
    private String pid;

    @Column(name = "seq_no")
    private Integer seqNo;

    @Column(name = "code_type" , length = 32)
    private String codeType;

    @Column(name = "base_code" , length = 64)
    private String baseCode;

    @Column(name = "code_desc" , length = 128)
    private String codeDesc;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "pid", foreignKey = @ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT))
    private Set<BaseDict> children;

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
