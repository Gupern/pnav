package com.gupern.pnav.common;

import lombok.Data;

@Data
public class User {
    private String access_token;

    private String name;

    private String sex;

    private String openId;

    private String headImgUrl;
}
