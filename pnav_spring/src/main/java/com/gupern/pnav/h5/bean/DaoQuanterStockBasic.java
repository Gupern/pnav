package com.gupern.pnav.h5.bean;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2023/4/19 20:55
 * @description: 股票代号列表
 */
@Data
@Entity
@Table(name = "quanter_stock_basic")
public class DaoQuanterStockBasic {
    @Id // 表示这是primary key
    private String id;
    @Column(name = "ts_code")
    private String tsCode;
    // 股票代号
    @Column(name = "symbol")
    private String symbol;
    // 股票名称
    @Column(name = "name")
    private String name;
    @Column(name = "area")
    private String area;
    @Column(name = "industry")
    private String industry;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "enname")
    private String enname;
    @Column(name = "cnspell")
    private String cnspell;
    @Column(name = "market")
    private String market;
    @Column(name = "exchange")
    private String exchange;
    @Column(name = "curr_type")
    private String currType;
    @Column(name = "list_status")
    private String listStatus;
    @Column(name = "list_date")
    private String listDate;
    @Column(name = "delist_date")
    private String delistDate;
    @Column(name = "is_hs")
    private String isHs;
    @Column(name = "tc_code")
    private String tcCode;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;
}
/*
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `ts_code` varchar(15) NOT NULL COMMENT 'tushare code，如000001.SZ',
  `symbol` varchar(10) NOT NULL COMMENT '股票代号',
  `name` varchar(30) NOT NULL COMMENT '股票名称',
  `area` varchar(15) DEFAULT NULL COMMENT '地域',
  `industry` varchar(15) DEFAULT NULL COMMENT '所属行业',
  `fullname` text COMMENT '股票全称',
  `enname` text COMMENT '英文全称',
  `cnspell` text COMMENT 'cnspell',
  `market` varchar(10) DEFAULT NULL COMMENT '市场类型（主板/创业板/科创板/CDR）',
  `exchange` varchar(5) DEFAULT NULL COMMENT '交易所代码',
  `curr_type` varchar(10) DEFAULT NULL COMMENT '交易货币',
  `list_status` varchar(5) DEFAULT NULL COMMENT '上市状态 L上市 D退市 P暂停上市',
  `list_date` varchar(20) DEFAULT NULL COMMENT '上市日期',
  `delist_date` varchar(20) DEFAULT NULL COMMENT '退市日期',
  `is_hs` varchar(5) DEFAULT NULL COMMENT '是否沪深港通标的，N否 H沪股通 S深股通',
  `tc_code` varchar(15) DEFAULT NULL COMMENT '腾讯股票code，如sz000001',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10238 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='股票列表，以tushare为主，扩展其他数据源字段'
 */