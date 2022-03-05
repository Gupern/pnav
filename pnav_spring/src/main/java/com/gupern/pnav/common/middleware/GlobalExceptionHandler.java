package com.gupern.pnav.common.middleware;

import com.gupern.pnav.common.bean.ResponseEnum;
import com.gupern.pnav.common.bean.ResultMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: Gupern
 * @date: 2022/3/6 2:46
 * @description:
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理请求第三方时的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = RequestException.class)
    @ResponseBody
    public ResultMsg requestExceptionHandler(HttpServletRequest req, RequestException e){
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ResultMsg.fail(e.getErrorCode(),e.getErrorMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public ResultMsg exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:{}", e.getMessage());
        return ResultMsg.fail(ResponseEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public ResultMsg exceptionHandler(HttpServletRequest req, Exception e){
        e.printStackTrace();
        log.error("其他异常！原因是:{}", e.getMessage());
        return ResultMsg.fail(ResponseEnum.INTERNAL_SERVER_ERROR);
    }
}

