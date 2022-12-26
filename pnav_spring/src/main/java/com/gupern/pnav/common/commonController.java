package com.gupern.pnav.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/common")
public class commonController {

    private final commonServiceImpl service;

    public commonController(commonServiceImpl service) {
        this.service = service;
    }

    @RequestMapping(value = "/loginOrRegister", method = RequestMethod.POST)
    @ResponseBody
    public Object loginOrRegister(@RequestBody JSONObject dto) {
        return service.loginOrRegister(dto);
    }
}
