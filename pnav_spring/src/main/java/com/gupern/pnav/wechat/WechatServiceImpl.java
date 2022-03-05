package com.gupern.pnav.wechat;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.Constant;
import com.gupern.pnav.common.bean.ResultMsg;
import com.gupern.pnav.common.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.gupern.pnav.wechat.util.WechatUtil.*;

@Service
public class WechatServiceImpl implements WechatService {
    private static Logger log = LoggerFactory.getLogger(WechatServiceImpl.class);

    // 公众号appid 从properties文件中获取
    @Value("${wechat.miniprogram.appid}")
    private String appId = "appId";
    // 公众号appsecert
    @Value("${wechat.miniprogram.appsecret.encoded}")
    private String appSecretEncoded = "asdfasdfasdf";

    @Value("${crypto.aes.key}")
    private String aesKey = "asdfasdfa";

    // 与接口配置信息中的Token要一致
    @Value("${wechat.miniprogram.token}")
    private String token = "pnav007";

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
     * @date: 2022/3/6 2:26
     * @description: 获取access_token TODO 不可暴露到互联网，只开发测试用
     */
    public Object getAccessToken() {
        return getMiniProgramAccessToken(appId, getAppSecret(aesKey, appSecretEncoded));
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 2:27
     * @description: 给小程序前端调用，获取openid
     */
    public Object getSession(JSONObject dto) {
        log.info("dto openid:{}", dto);
        String code = dto.getString("code");
        log.info("dto openid:{}", code);
        return getMiniProgramSession(code, appId, getAppSecret(aesKey, appSecretEncoded));
    }

    /*
     * @author: Gupern
     * @date: 2022/3/5 18:41
     * @description: 微信小程序推送消息
     */
    public Object miniprogramPush(HttpServletRequest request, JSONObject dto) {
        String requestMethod = request.getMethod();
        // 如果是GET方式，取parameter
        try {
            if ("GET".equals(requestMethod)) {
                dto = new JSONObject(); // GET 请求dto可能为null
                dto.put("echostr", request.getParameter("echostr"));
                dto.put("nonce", request.getParameter("nonce"));
                dto.put("timestamp", request.getParameter("timestamp"));
                dto.put("signature", request.getParameter("signature"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail(Constant.RESPONSE_FAILED_CODE,Constant.RESPONSE_FAILED_MSG);
        }

        log.info(dto.toString());
        if (checkSignature(dto, token)) {
            return dto.getString("echostr");
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
