package com.gupern.pnav.h5;

import org.springframework.stereotype.Service;

@Service
public class H5ServiceImpl implements H5Service {
    @Override
    public Object sayHelloWorld() {
        return "Hello world!";
    }
}
