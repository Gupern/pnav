package com.gupern.pnav.wechat.bean;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2022/3/8 13:00
 * @description: 任务信息实体
 */
@Data
@Entity
@Table(name = "task_info")
public class DaoTaskInfo {
    @Id // 表示这是primary key
//    @GeneratedValue(strategy = GenerationType.AUTO) // 表示自增
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String project;
    private String task;
    private String comment;
    private int status;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;

}
