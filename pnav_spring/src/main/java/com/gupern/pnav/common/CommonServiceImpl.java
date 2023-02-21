package com.gupern.pnav.common;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.DaoUserInfo;
import com.gupern.pnav.common.bean.RepositoryUserInfo;
import com.gupern.pnav.common.bean.ResponseEnum;
import com.gupern.pnav.common.bean.ResultMsg;
import com.gupern.pnav.common.util.CryptoUtil;
import com.gupern.pnav.common.util.RedisUtil;
import com.gupern.pnav.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService {
    private static Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);
    @Autowired
    private RepositoryUserInfo repositoryUserInfo;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Object loginOrRegister(JSONObject dto) {
        // 获取手机号密码
        String phone = dto.getString("phone");
        // 校验手机号格式
        if (!StringUtil.isPhoneFormat(phone)) {
            return ResultMsg.fail("400", "手机号格式不正确，请重新输入");
        }
        String password = dto.getString("password");
        // 类型： 登录还是注册 默认为登录
        String type = "login";
        // 根据手机号查sql
        DaoUserInfo userInfo = repositoryUserInfo.findByPhone(phone);
        // 若查不到手机号，则创建账户，返回token
        if (userInfo == null) {
            userInfo = new DaoUserInfo();
            userInfo.setId(CryptoUtil.getUUID());
            userInfo.setPhoneNumber(phone);
            userInfo.setPassword(password);
            repositoryUserInfo.save(userInfo);
            type = "register";
        } else { // 如果查得到，则校验password，若校验成功，则返回token
            String passwordInDb = userInfo.getPassword();
            // 若校验不成功，则返回密码不对
            if (!password.equals(passwordInDb)) {
                return ResultMsg.fail("400", "密码不正确，请重新输入");
            }
        }
        // 校验密码成功或者创建用户成功，则返回token，有效期2m=120s
        String token = CryptoUtil.getUUID();
        redisUtil.set(token, userInfo, 120L);
        JSONObject returnJson = new JSONObject();
        returnJson.put("token", token);
        returnJson.put("type", type);
        return ResultMsg.success(ResponseEnum.REQUEST_SUCCEED, returnJson);
    }
}
