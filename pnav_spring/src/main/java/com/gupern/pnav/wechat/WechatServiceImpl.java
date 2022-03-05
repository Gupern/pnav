package com.gupern.pnav.wechat;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.Constant;
import com.gupern.pnav.common.bean.ResultMsg;
import com.gupern.pnav.common.util.CryptoUtil;
import com.gupern.pnav.wechat.bean.DtoWechatMiniprogramPush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.gupern.pnav.wechat.util.SignUtil.checkSignature;

@Service
public class WechatServiceImpl implements WechatService {
    private static Logger log = LoggerFactory.getLogger(WechatServiceImpl.class);

    // 与接口配置信息中的Token要一致
    @Value("${wechat.miniprogram.token}")
    private String token = "pnav007";

    // 公众号appid 从properties文件中获取
    @Value("${wechat.miniprogram.appid}")
    private String appId = "appId";
    // 公众号appsecert
    @Value("${wechat.miniprogram.appsecret.encoded}")
    private String appSecretEncoded = "asdfasdfasdf";

    @Value("${crypto.aes.key}")
    private String aesKey = "asdfasdfa";

    public Object sayHelloWorld() {
        System.out.println(aesKey);
        System.out.println(appId);
        System.out.println(appSecretEncoded);
        String appSecret = getAppSecret(aesKey,appSecretEncoded);
        System.out.println(appSecret);
        return "hello world";
    }

    /*
     * @author: Gupern
     * @date: 2022/3/5 18:41
     * @description: 微信小程序推送消息
     */
    public Object miniprogramPush(HttpServletRequest request, JSONObject postJson) {
        String requestMethod = request.getMethod();
        DtoWechatMiniprogramPush dto = new DtoWechatMiniprogramPush();
        // 如果是GET方式，取parameter
        try {
            if ("GET".equals(requestMethod)) {
                dto.setEchostr(request.getParameter("echostr"));
                dto.setNonce(request.getParameter("nonce"));
                dto.setTimestamp(request.getParameter("timestamp"));
                dto.setSignature(request.getParameter("signature"));
            } else { // 如果是POST方式，取json
                dto.setSignature(postJson.getString("signature"));
                dto.setNonce(postJson.getString("nonce"));
                dto.setTimestamp(postJson.getString("timestamp"));
                dto.setEchostr(postJson.getString("echostr"));
            }
        } catch (Exception e) {
            return ResultMsg.fail(Constant.RESPONSE_FAILED_CODE,Constant.RESPONSE_FAILED_MSG);
        }

        log.info(dto.toString());
        if (checkSignature(dto, token)) {
            return dto.getEchostr();
        } else {
            return ResultMsg.fail(Constant.RESPONSE_FAILED_CODE, Constant.RESPONSE_WECHAT_CHECK_FAILED_MSG);
        }
    }

    /*
     * @author: Gupern
     * @date: 2022/3/5 18:12
     * @description: 解密appsecret
     */
    private String getAppSecret(String aesKey, String appSecretEncoded){
        CryptoUtil cryptoUtil = CryptoUtil.getInstance(null);
        return cryptoUtil.AESdecode(appSecretEncoded,aesKey);
    }
}
