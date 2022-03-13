package com.gupern.pnav.wechat;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface WechatService {
    Object sayHelloWorld();

    Object miniprogramPush(HttpServletRequest httpServletRequest, JSONObject dto);
    Object getAccessToken();
    Object getSession(JSONObject dto);
    Object getTask(JSONObject dto);
}
