package com.gupern.pnav.wechat.util;

import com.gupern.pnav.wechat.bean.DtoWechatMiniprogramPush;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.gupern.pnav.common.util.CryptoUtil.byteToStr;

/**
 * 请求校验工具类
 */
public class SignUtil {
    private static Logger log = LoggerFactory.getLogger(SignUtil.class);
    /*
     * @author: Gupern
     * @date: 2022/3/5 19:01
     * @description: 对微信请求进行合法性验证
           算法
           1. 将token、timestamp、nonce三个参数的值进行字典序排序
           2. 将三个参数字符串拼接成一个字符串进行sha1加密
           3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    public static boolean checkSignature(DtoWechatMiniprogramPush dto, String token) {
        String timestamp = dto.getTimestamp();
        String nonce = dto.getNonce();
        String signature = dto.getSignature();
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
        System.out.println("-----执行微信签名加密认证-----");
        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

}