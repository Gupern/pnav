package com.gupern.pnav.wechat.bean;

import lombok.Data;
/**
 * 授权用户对象
 */
@Data
public class PojoAuthorizedUsers {

    private String access_token;

    private String name;

    private String sex;

    private String openId;

    private String headImgUrl;

}