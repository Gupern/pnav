package com.gupern.pnav.h5.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2022/11/5 22:51
 * @description: 份额运营表实体
 */
@Data
@Entity
@Table(name = "shares_running")
public class DaoSharesRunning {
    @Id // 表示这是primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 表示自增
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "fund_name")
    private String fundName;
    @Column(name = "fund_code")
    private String fundCode;
    @Column(name = "purchase_time")
    // 增加此注释， 转换时间戳String类型为Date类型
    @JsonFormat(locale = "zh", pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date purchaseTime;

    @Column(name = "unv")
    private float unv;
    @Column(name = "shares")
    private float shares;
    @Column(name = "shares_remaining")
    private float sharesRemaining;

    @Column(name = "zero_cost")
    private int zeroCost;

    @Column(name = "fund_record_id")
    private int fundRecordId;

    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;
}
