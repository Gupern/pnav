package com.gupern.pnav.wechat;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface WechatService {
    Object sayHelloWorld();

    Object miniprogramPush(HttpServletRequest httpServletRequest, JSONObject dto);
    Object getAccessToken();
    Object getSession(JSONObject dto);
    Object getTask(JSONObject dto);
    Object finishTask(JSONObject dto);
    Object changeTask(JSONObject dto);
    Object getPersonalProjectInfo(JSONObject dto);
    Object updatePersonalProjectInfo(JSONObject dto);

    Object recList(JSONObject dto);
    Object recHisList(JSONObject dto);
    Object recHisDetail(JSONObject dto);
}
