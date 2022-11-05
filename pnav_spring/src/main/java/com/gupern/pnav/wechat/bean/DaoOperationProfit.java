package com.gupern.pnav.wechat.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2022/11/5 22:49
 * @description: 操作盈亏表实体
 */
@Data
@Entity
@Table(name = "operation_profit")
public class DaoOperationProfit {
    @Id // 表示这是primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 表示自增
    private int id;
    private String openid;
    @Column(name = "fund_name")
    private String fundName;
    @Column(name = "fund_code")
    private String fundCode;
    @Column(name = "unv")
    private float unv;
    @Column(name = "unv_sold")
    private float unvSold;
    @Column(name = "shares")
    private float shares;
    @Column(name = "service_charge")
    private float serviceCharge;
    @Column(name = "profit")
    private float profit;
    @Column(name = "operation_time")
    // 增加此注释， 转换时间戳String类型为Date类型
    @JsonFormat(locale = "zh", pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date operationTime;

    @Column(name = "fund_record_id")
    private int fundRecordId;

    // 增加此注解，否则会为null
    @CreationTimestamp
    @JsonFormat(locale = "zh", pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @JsonFormat(locale = "zh", pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "updated_time")
    private Date updatedTime;
}
