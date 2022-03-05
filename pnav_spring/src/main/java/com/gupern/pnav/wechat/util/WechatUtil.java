package com.gupern.pnav.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

import static com.gupern.pnav.common.util.CryptoUtil.byteToStr;

public class WechatUtil {
    private static final Logger log = LoggerFactory.getLogger(WechatUtil.class);

    private static final RestTemplate restTemplate = RequestUtil.getInstance().myRestTemplate;

    /*
     * @author: Gupern
     * @date: 2022/3/5 19:01
     * @description: 对微信请求进行合法性验证
           算法
           1. 将token、timestamp、nonce三个参数的值进行字典序排序
           2. 将三个参数字符串拼接成一个字符串进行sha1加密
           3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    public static boolean checkSignature(JSONObject dto, String token) {
        String timestamp = dto.getString("timestamp");
        String nonce = dto.getString("nonce");
        String signature = dto.getString("signature");
        String[] arr = new String[]{token, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info("-----执行微信签名加密认证-----:{}", token);
        content = null; // 清空content
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 0:35
     * @description: 获取小程序的access_token
     */
    public static String getMiniProgramAccessToken(String appId, String appSecret) {
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, appSecret);
        log.info("url:{}", url);
        JSONObject resJson = restTemplate.getForObject(url, JSONObject.class);
        log.info("resJson:{}", resJson);
        return Objects.requireNonNull(resJson).getString("access_token");
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 2:35
     * @description: 获取小程序的openid, 直接透传微信服务器的返回
     *              openid, session_key, unionid, errcode, errmsg
     */
    public static Object getMiniProgramSession(String code, String appId, String appSecret) {
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", appId, appSecret, code);
        log.info("url:{}", url);
        JSONObject resJson = restTemplate.getForObject(url, JSONObject.class);
        log.info("resJson:{}", resJson);
        return resJson;
    }
}
