package com.gupern.pnav.wechat.bean;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author: Gupern
 * @date: 2022/3/8 13:00
 * @description: 用户信息实体
 */
@Data
@Entity
@Table(name = "user_info")
public class DaoUserInfo {
    @Id // 表示这是primary key
//    @GeneratedValue(strategy = GenerationType.AUTO) // 表示自增
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String openid;
    @Column(name = "session_key")
    private String sessionKey;
    private String unionid;
    @Column(name = "user_info")
    private String userInfo;
    @Column(name = "user_location")
    private String userLocation;
    private String address;
    private String invoice_title;
    private String werun;
    private String record;
    @Column(name = "write_photos_album")
    private String writePhotosAlbum;
    private String camera;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    private String gender;
    private String city;
    private String province;
    private String country;
    private String language;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "pure_phone_number")
    private String purePhoneNumber;
    @Column(name = "country_code")
    private String countryCode;
    // 增加此注解，否则会为null
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @UpdateTimestamp
    @Column(name = "updated_time")
    private Date updatedTime;
}