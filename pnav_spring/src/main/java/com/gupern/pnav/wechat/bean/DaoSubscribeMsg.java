package com.gupern.pnav.wechat.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2022/3/8 13:15
 * @description: 订阅消息实体
 */

@Data
@Entity(name = "SubscribeMsg")
@Table(name = "subscribe_msg") // 指定表名
public class DaoSubscribeMsg {
    @Id // 表示这是primary key
    // 数据库中已经设置为自增，所以要用IDENTITY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "to_user_name")
    private String toUserName;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "template_id")
    private String templateId;
    @Column(name = "subscribe_status_string")
    private String subscribeStatusString;
    @Column(name = "popup_scene")
    private String popupScene;
    @Column(name = "from_user_name")
    private String fromUserName;
    private String event;
    @Column(name = "msg_type")
    private String msgType;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;

}
