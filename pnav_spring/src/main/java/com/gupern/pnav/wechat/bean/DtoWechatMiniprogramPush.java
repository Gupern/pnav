package com.gupern.pnav.wechat.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: guper
 * @date: 2022/3/5 20:41
 * @description: 微信小程序消息推送过来的请求体
 */
@Data
public class DtoWechatMiniprogramPush {
    public String signature;
    public String timestamp;
    public String nonce;
    public String echostr;
}
