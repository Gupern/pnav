package com.gupern.pnav.wechat;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/wechat")
public class WechatController {
    private final WechatServiceImpl service;

    public WechatController(WechatServiceImpl service) {
        this.service = service;
    }

    @RequestMapping(value = "/hello", method = GET)
    @ResponseBody
    public Object sayHelloWorld() {
        return service.sayHelloWorld();
    }

    @RequestMapping(value = "/miniprogram/push", method = {GET, POST})
    @ResponseBody // 兼容post方式和get方式
    public Object miniprogramPush(HttpServletRequest httpServletRequest, @RequestBody(required = false) JSONObject postJson) {
        return service.miniprogramPush(httpServletRequest, postJson);
    }
}
