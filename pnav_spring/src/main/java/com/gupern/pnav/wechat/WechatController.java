package com.gupern.pnav.wechat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
public class WechatController {
    private final WechatServiceImpl service;

    public WechatController(WechatServiceImpl service) {
        this.service = service;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public Object sayHelloWorld() {
        return service.sayHelloWorld();
    }
}
