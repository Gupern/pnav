package com.gupern.pnav.common.util;

import com.gupern.pnav.common.bean.DaoUserInfo;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserRequest {
    private static final Logger log = LoggerFactory.getLogger(User.class);
    public static String getCurrentToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String token = request.getHeader("token");
        return token;
    }

    public static String getPhoneInToken(RedisUtil redisUtil) {
        // 手机号从token中获取
        String token = UserRequest.getCurrentToken();
        DaoUserInfo userInfo = (DaoUserInfo) redisUtil.get(token);
        String phone = userInfo.getPhoneNumber();
        return phone;
    }
    public static DaoUserInfo getUserInfoInToken(RedisUtil redisUtil) {
        // 手机号从token中获取
        String token = UserRequest.getCurrentToken();
        DaoUserInfo userInfo = (DaoUserInfo) redisUtil.get(token);
        return userInfo;
    }

    public static String getUserIdInToken(RedisUtil redisUtil) {
        // 手机号从token中获取
        String token = UserRequest.getCurrentToken();
        DaoUserInfo userInfo = (DaoUserInfo) redisUtil.get(token);
        String userId = userInfo.getId();
        return userId;
    }
}
