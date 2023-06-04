package com.gupern.pnav.wechat.bean;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2023/6/4 05:24
 * @description: 股票推荐策略实体
 */
@Data
@Entity(name = "StockStrategy")
@Table(name = "stock_strategy") // 指定表名
public class DaoStockStrategy {
    @Id // 表示这是primary key
    // 数据库中已经设置为自增，所以要用IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "strategy_name")
    private String strategyName;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;

}
