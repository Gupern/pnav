package com.gupern.pnav.wechat;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.Constant;
import com.gupern.pnav.common.bean.ResponseEnum;
import com.gupern.pnav.common.bean.ResultMsg;
import com.gupern.pnav.common.util.CryptoUtil;
import com.gupern.pnav.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.gupern.pnav.wechat.util.WechatUtil.getMiniProgramAccessToken;
import static com.gupern.pnav.wechat.util.WechatUtil.getMiniProgramSession;

@Service
public class WechatServiceImpl implements WechatService {
    private static Logger log = LoggerFactory.getLogger(WechatServiceImpl.class);

    // 公众号appid 从properties文件中获取
    @Value("${wechat.miniprogram.appid}")
    private String appId = "appId";
    // 公众号appsecert
    @Value("${wechat.miniprogram.appsecret.encoded}")
    private String appSecretEncoded = "asdfasdfasdf";

    // 与接口配置信息中的Token要一致
    @Value("${wechat.miniprogram.token}")
    private String token = "pnav007";

    public Object sayHelloWorld() {
        System.out.println(appId);
        System.out.println(appSecretEncoded);
        return "hello world";
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 2:26
     * @description: 获取access_token TODO 不可暴露到互联网，只开发测试用
     */
    public Object getAccessToken() {
        try {
            return getMiniProgramAccessToken(appId, CryptoUtil.getInstance(null).Base64Decode(appSecretEncoded));
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMsg.fail(ResponseEnum.REQUEST_FAILED.getCode(), ResponseEnum.REQUEST_FAILED.getMsg());
        }
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
        try {
            return getMiniProgramSession(code, appId, CryptoUtil.getInstance(null).Base64Decode(appSecretEncoded));
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMsg.fail(ResponseEnum.REQUEST_FAILED.getCode(), ResponseEnum.REQUEST_FAILED.getMsg());
        }
    }

    /*
     * @author: Gupern
     * @date: 2022/3/5 18:41
     * @description: 微信小程序推送消息
     */
    public Object miniprogramPush(HttpServletRequest request, JSONObject dto) {
        try {
            String echostr = request.getParameter("echostr");
            String nonce = request.getParameter("nonce");
            String timestamp = request.getParameter("timestamp");
            String signature = request.getParameter("signature");
            String msgSignature = request.getParameter("msg_signature");
            String appSecret = CryptoUtil.getInstance(null).Base64Decode(appSecretEncoded);
            log.info("nonce:{}, timestamp:{}, signature:{}, appSecret:{}, msg_signature:{}", nonce, timestamp, signature, appSecret, msgSignature);
            // TODO 1.  解密
            WechatUtil wechatUtil = new WechatUtil(token, appSecret, appId);
            String planeText = wechatUtil.decryptMsg(msgSignature, timestamp, nonce, dto);
            log.info("planeText: {}", planeText);
            // TODO 2.  并接收入库
            // TODO 3.  返回正式数据

            /*
             接入验证时：若确认此次 GET 请求来自微信服务器，请原样返回 echostr 参数内容，则接入生效，成为开发者成功，否则接入失败。
             return echostr;
             接入成功后，服务器收到请求必须做出下述回复，这样微信服务器才不会对此作任何处理，并且不会发起重试，否则，将出现严重的错误提示。详见下面说明：
             1. 直接回复success（推荐方式）
             2. 直接回复空串（指字节长度为0的空字符串，而不是结构体中content字段的内容为空）
             3. 若接口文档有指定返回内容，应按文档说明返回
             */
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMsg.fail(Constant.RESPONSE_FAILED_CODE, Constant.RESPONSE_WECHAT_CHECK_FAILED_MSG);
        }
    }
}
