package com.gupern.pnav.h5.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2022/3/8 13:01
 * @description: 基金记录实体
 */
@Data
@Entity
@Table(name = "fund_record")
public class DaoFundRecord {
    @Id // 表示这是primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 表示自增
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "fund_name")
    private String fundName;
    @Column(name = "fund_code")
    private String fundCode;
    private int operation;
    @Column(name = "amount")
    private float amount;
    @Column(name = "unv")
    private float unv;
    @Column(name = "shares")
    private float shares;
    private int status;
    @Column(name = "operation_time")
    // 增加此注释， 转换时间戳String类型为Date类型
    @JsonFormat(locale = "zh", pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date operationTime;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;
}
