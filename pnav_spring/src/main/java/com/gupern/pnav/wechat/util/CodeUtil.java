package com.gupern.pnav.wechat.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.wechat.bean.PojoAuthorizedUsers;
import com.gupern.pnav.wechat.bean.PojoConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeUtil{
    private static Logger log = LoggerFactory.getLogger(CodeUtil.class);
    //通过code获取用户信息
    public static PojoAuthorizedUsers getUserInfo(String code) {
        PojoAuthorizedUsers pojoAuthorizedUsers = new PojoAuthorizedUsers();

        // 2. 通过code换取网页授权access_token
        if (code != null || !(code.equals(""))) {

            // 替换字符串，获得请求access token URL
            String tokenUrl = UserInfoUtil.getWebAccess(PojoConstants.APPID, PojoConstants.APPSECRET, code);
            log.info("第二步:get Access Token URL:{}", tokenUrl);

            // 通过https方式请求获得web_access_token
            JSONObject jsonObject = WechatUtil.httpRequest(tokenUrl, "GET", null);

            log.info("请求到的Access Token:{}", jsonObject.toString());

            if (null != jsonObject) {
                try {
                    String WebAccessToken = jsonObject.getString("access_token");
                    String openId = jsonObject.getString("openid");
                    log.info("WebAccessToken:{} , openId:{}", WebAccessToken, openId);
                    pojoAuthorizedUsers.setAccess_token(WebAccessToken);
                    pojoAuthorizedUsers.setOpenId(openId);
                    // 3. 使用获取到的 Access_token 和 openid 拉取用户信息
                    String userMessageUrl = UserInfoUtil.getUserMessage(WebAccessToken, openId);
                    log.info("第三步:获取用户信息的URL:{}", userMessageUrl);

                    // 通过https方式请求获得用户信息响应
                    JSONObject userinfoResponse = WechatUtil.httpRequest(userMessageUrl, "GET", null);

                    pojoAuthorizedUsers = WechatUtil.convertUserInfo(userinfoResponse.toString(),WebAccessToken);
                } catch (JSONException e) {
                    log.error("获取Web Access Token失败");
                }
            }
        }
        return pojoAuthorizedUsers;
    }
}
