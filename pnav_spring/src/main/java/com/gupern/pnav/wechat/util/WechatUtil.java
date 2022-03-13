package com.gupern.pnav.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.util.*;
import com.gupern.pnav.wechat.bean.RepositoryTaskInfoMsg;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.gupern.pnav.common.util.CryptoUtil.*;
import static com.gupern.pnav.common.util.SHA1.getSHA1;

public class WechatUtil {
    private static final Logger log = LoggerFactory.getLogger(WechatUtil.class);

    private static final RestTemplate restTemplate = RequestUtil.getInstance().myRestTemplate;

    @Autowired
    private RepositoryTaskInfoMsg repositoryTaskInfoMsg;

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
        MessageDigest md;
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
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 0:35
     * @description: 请求微信服务器，获取最新的小程序的access_token
     * TODO 搭建redis，增加token到缓存
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
     * @date: 2022/3/13 12:24
     * @description:  TODO 从cache中获取accessToken
     * 有效期2小时
     */
    @CachePut(cacheNames = "local", key = "#appId")
    public String getMpCacheToken(String appId, String appSecret) {
        return "";
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

    // 以下内容引用微信官方加解密包，主要为了改写解密时兼容json格式
    Charset CHARSET = StandardCharsets.UTF_8;
    Base64 base64 = new Base64();
    byte[] aesKey;
    String token;
    String appId;

    /**
     * 构造函数
     *
     * @param token          公众平台上，开发者设置的token
     * @param encodingAesKey 公众平台上，开发者设置的EncodingAESKey
     * @param appId          公众平台appid
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public WechatUtil(String token, String encodingAesKey, String appId) throws AesException {
        if (encodingAesKey.length() != 43) {
            throw new AesException(AesException.IllegalAesKey);
        }

        this.token = token;
        this.appId = appId;
        aesKey = Base64.decodeBase64(encodingAesKey + "=");
    }

    // 随机生成16位字符串
    String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 对明文进行加密.
     *
     * @param text 需要加密的明文
     * @return 加密后base64编码的字符串
     * @throws AesException aes加密失败
     */
    public String encrypt(String randomStr, String text) throws AesException {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = randomStr.getBytes(CHARSET);
        byte[] textBytes = text.getBytes(CHARSET);
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
        byte[] appidBytes = appId.getBytes(CHARSET);

        // randomStr + networkBytesOrder + text + appid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(appidBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            String base64Encrypted = base64.encodeToString(encrypted);

            return base64Encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.EncryptAESError);
        }
    }

    /**
     * 对密文进行解密.
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     * @throws AesException aes解密失败
     */
    public String decrypt(String text) throws AesException {
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.decodeBase64(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.DecryptAESError);
        }

        String xmlContent, from_appid;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(original);

            // 分离16位随机字符串,网络字节序和AppId
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int xmlLength = recoverNetworkBytesOrder(networkOrder);

            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), CHARSET);
            from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length),
                    CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.IllegalBuffer);
        }

        // appid不相同的情况
        if (!from_appid.equals(appId)) {
            throw new AesException(AesException.ValidateAppidError);
        }
        return xmlContent;

    }

    /**
     * 将公众平台回复用户的消息加密打包.
     * <ol>
     * 	<li>对要发送的消息进行AES-CBC加密</li>
     * 	<li>生成安全签名</li>
     * 	<li>将消息密文和安全签名打包成xml格式</li>
     * </ol>
     *
     * @param replyMsg  公众平台待回复用户的消息，xml格式的字符串
     * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
     * @param nonce     随机串，可以自己生成，也可以用URL参数的nonce
     * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的xml格式的字符串
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public String encryptMsg(String replyMsg, String timeStamp, String nonce) throws AesException {
        // 加密
        String encrypt = encrypt(getRandomStr(), replyMsg);

        // 生成安全签名
        if (timeStamp == "") {
            timeStamp = Long.toString(System.currentTimeMillis());
        }

        String signature = getSHA1(token, timeStamp, nonce, encrypt);

        // System.out.println("发送给平台的签名是: " + signature[1].toString());
        // 生成发送的xml
        String result = XMLParse.generate(encrypt, signature, timeStamp, nonce);
        return result;
    }

    /**
     * 检验消息的真实性，并且获取解密后的明文.
     * <ol>
     * 	<li>利用收到的密文生成安全签名，进行签名验证</li>
     * 	<li>若验证通过，则提取xml中的加密消息</li>
     * 	<li>对消息进行解密</li>
     * </ol>
     *
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timeStamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @param postData     密文，对应POST请求的数据
     * @return 解密后的原文
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public String decryptMsg(String msgSignature, String timeStamp, String nonce, String postData)
            throws AesException {

        // 密钥，公众账号的app secret
        // 提取密文
        Object[] encrypt = XMLParse.extract(postData);

        // 验证安全签名
        String signature = getSHA1(token, timeStamp, nonce, encrypt[1].toString());

        // 和URL中的签名比较是否相等
        // System.out.println("第三方收到URL中的签名：" + msg_sign);
        // System.out.println("第三方校验签名：" + signature);
        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }

        // 解密
        String result = decrypt(encrypt[1].toString());
        return result;
    }

    public String decryptMsg(String msgSignature, String timeStamp, String nonce, JSONObject postData)
            throws AesException {

        // 密钥，公众账号的app secret
        // 提取密文
        String encrypt = postData.getString("Encrypt");
        log.info("encrypt:{}", encrypt);

        // 验证安全签名
        String signature = getSHA1(token, timeStamp, nonce, encrypt);
        log.info("signature:{}", signature);

        // 和URL中的签名比较是否相等
        // System.out.println("第三方收到URL中的签名：" + msg_sign);
        // System.out.println("第三方校验签名：" + signature);
        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }

        // 解密
        String result = decrypt(encrypt);
        return result;
    }

    /**
     * 验证URL
     *
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timeStamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @param echoStr      随机串，对应URL参数的echostr
     * @return 解密之后的echostr
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public String verifyUrl(String msgSignature, String timeStamp, String nonce, String echoStr)
            throws AesException {
        String signature = getSHA1(token, timeStamp, nonce, echoStr);

        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }

        String result = decrypt(echoStr);
        return result;
    }
    // 引用微信官方包结束

    /*
     * @author: Gupern
     * @date: 2022/3/12 23:30
     * @description: 推送小程序消息，调用subscribeMessage.send接口
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
     */
    public static boolean sendSubscribeMsg(String accessToken, JSONObject requestJson) {
        // accessToken在url里
        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=%s", accessToken);
        log.info("url:{}", url);
        // 其他参数在post json里
        JSONObject responseJson = restTemplate.postForObject(url, requestJson, JSONObject.class);
        log.info(responseJson.toString());
        return false;
    }


    /*
     * @author: Gupern
     * @date: 2022/3/13 14:22
     * @description: 根据openid随机获取该用户的一个task
     */
    public static String getRandomTask(List<JSONObject> allTasksList) {
        // 获取随机事项
        Random random = new Random();
        if (allTasksList.size() <= 0) {
            log.info("there are no tasks");
            return null;
        }
        int n = random.nextInt(allTasksList.size());
        return allTasksList.get(n).getString("task");
    }

}
