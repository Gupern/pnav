package com.gupern.pnav.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
public class CommonController {

    private final CommonServiceImpl service;

    public CommonController(CommonServiceImpl service) {
        this.service = service;
    }

    @RequestMapping(value = "/loginOrRegister", method = RequestMethod.POST)
    @ResponseBody
    public Object loginOrRegister(@RequestBody JSONObject dto) {
        return service.loginOrRegister(dto);
    }
}
