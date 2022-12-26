package com.gupern.pnav.common.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Gupern
 * @date: 2022/3/6 3:08
 * @description: 用于特殊请求
 */
public class RequestUtil {

    // 单例
    public static RequestUtil me;
    public final RestTemplate myRestTemplate = new RestTemplate();
    private RequestUtil() {
        myRestTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
    }
    //双重锁
    public static RequestUtil getInstance() {
        if (me == null) {
            synchronized (RequestUtil.class) {
                if (me == null) {
                    me = new RequestUtil();
                }
            }
        }
        return me;
    }

    // 继承 MappingJackson2HttpMessageConverter 并在构造过程中设置其支持的 MediaType 类型
    private static class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public WxMappingJackson2HttpMessageConverter(){
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.TEXT_PLAIN);
            setSupportedMediaTypes(mediaTypes);// tag6
        }
    }

    public static HttpHeaders getHeader() {
        //设置header信息
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return requestHeaders;
    }
}
