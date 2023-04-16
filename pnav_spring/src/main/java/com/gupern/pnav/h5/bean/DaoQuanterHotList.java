package com.gupern.pnav.h5.bean;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2023/4/16 22:07
 * @description: 热股列表
 */
@Data
@Entity
@Table(name = "quanter_hot_list")
public class DaoQuanterHotList {
    @Id // 表示这是primary key
    private String id;
    private String source;
    @Column(name = "symbol_list")
    private String symbolList;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;
}
