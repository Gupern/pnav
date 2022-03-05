package com.gupern.pnav.wechat;

import org.springframework.stereotype.Service;

@Service
public class WechatServiceImpl implements WechatService {
    public Object sayHelloWorld() {
        return "hello world";
    }
}
