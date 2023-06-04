package com.gupern.pnav.wechat.bean;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2023/6/4 05:24
 * @description: 股票推荐详情实体
 */
@Data
@Entity(name = "StockRecommendDetail")
@Table(name = "stock_recommend_detail") // 指定表名
public class DaoStockRecommendDetail {
    @Id // 表示这是primary key
    // 数据库中已经设置为自增，所以要用IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @CreationTimestamp
    @Column(name = "recommend_time")
    private Date recommendTime;
    @Column(name = "stock_name")
    private String stockName;
    @Column(name = "stock_code")
    private String stockCode;
    @Column(name = "strategy_id")
    private int strategyId;
    @Column(name = "recommend_operation")
    private int recommendOperation;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;

}
