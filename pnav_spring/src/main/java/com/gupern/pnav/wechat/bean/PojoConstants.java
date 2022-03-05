package com.gupern.pnav.wechat.bean;

import org.springframework.beans.factory.annotation.Value;

public class PojoConstants {
    // 公众号appid 从properties文件中获取
    @Value("${wechat.official_account.appid}")
    public static final String APPID = "";
    // 公众号appsecert
    @Value("${wechat.official_account.appsecret}")
    public static final String APPSECRET = "";
    // 静默授权
    public static final String SCOPE_BASE = "snsapi_base";
    // 获取用户信息会提示确认授权
    public static final String SCOPE_USERINFO = "snsapi_userinfo";
    // 授权回调地址-服务器域名
    public static String BACKURL = "http://n2zdi6.natappfree.cc/user/userInfo";
    // 获取access_token_Url
    private static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // 将appid与appsecert填入后得到的URL
    public static String getAccess_token_url(){
        return ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET",APPSECRET);
    }
}
