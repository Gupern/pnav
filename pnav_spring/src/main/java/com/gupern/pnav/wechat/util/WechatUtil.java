package com.gupern.pnav.wechat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.wechat.bean.PojoAuthorizedUsers;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

public class WechatUtil {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(WechatUtil.class);
    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new WechatX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    /**
     * 组装用户授权信息<br>
     *
     * @param userMessageResponse 授权用户响应数据
     * @return authorizedUsersBean {@link PojoAuthorizedUsers 授权用户对象}
     * @throws JSONException json转换异常
     * @since 2017/10/10
     */
    public static PojoAuthorizedUsers convertUserInfo(String userMessageResponse, String accessToken) {
        PojoAuthorizedUsers pojoAuthorizedUsers = new PojoAuthorizedUsers();

        JSONObject userMessageJsonObject = JSON.parseObject(userMessageResponse);

        log.info("用户信息:{}", userMessageJsonObject.toJSONString());

        if (userMessageJsonObject != null) {
            try {
                //用户昵称
                String nickName = userMessageJsonObject.getString("nickname");
                //用户性别
                String sex = userMessageJsonObject.getString("sex");
                sex = (sex.equals("1")) ? "男" : "女";
                //用户唯一标识
                String openid = userMessageJsonObject.getString("openid");
                String headimgurl = userMessageJsonObject.getString("headimgurl");
                pojoAuthorizedUsers.setAccess_token(accessToken);
                pojoAuthorizedUsers.setOpenId(openid);
                pojoAuthorizedUsers.setName(nickName);
                pojoAuthorizedUsers.setSex(sex);
                pojoAuthorizedUsers.setHeadImgUrl(headimgurl);
                log.info("用户昵称:{}, 用户性别:{}, OpenId:{}, 用户头像：{}", nickName, sex, openid, headimgurl);
            } catch (JSONException e) {
                log.error("获取用户信息失败");
            }
        }

        return pojoAuthorizedUsers;
    }
}
