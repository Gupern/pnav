package com.gupern.pnav.common.middleware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author: Gupern
 * @date: 2022/3/6 1:54
 * @description: 全局异常类 注解
 */
@ControllerAdvice
public class MyExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);
    @ExceptionHandler(value =Exception.class)
    public String exceptionHandler(Exception e){
        e.printStackTrace();
        log.error("[ERROR] 异常！原因是: {}", e.getMessage());
        return e.getMessage();
    }
}


