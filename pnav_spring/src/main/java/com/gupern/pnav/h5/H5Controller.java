package com.gupern.pnav.h5;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/h5")
public class H5Controller {

    private final H5ServiceImpl h5Service;

    public H5Controller(H5ServiceImpl h5Service) {
        this.h5Service = h5Service;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public Object sayHelloWorld() {
        return h5Service.sayHelloWorld();
    }

}
