package com.gupern.pnav.common.middleware;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.wechat.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author: Gupern
 * @date: 2022/3/6 14:44
 * @description: API请求拦截器，为所有的请求打印日志，记录耗时
 */
@Order(2)
@Component
public class ApiInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(WechatUtil.class);
    private static final String prefix = "[--ApiInterceptor log--] ";

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 运行 Controller 某个方法时，方法抛出异常将不进入此方法
        long start = (long) request.getAttribute("startTime");
        log.info(prefix + "TimeInterceptor 处理时长为：" + (new Date().getTime() - start));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info(prefix + "TimeInterceptor 完成 Controller 某个方法");
        long start = (long) request.getAttribute("startTime");
        log.info("TimeInterceptor 处理时长为：" + (new Date().getTime() - start) + "milliseconds");
    }

    /*
     * @author: Gupern
     * @date: 2022/3/6 15:00
     * @description: 打印request信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hObject) {
        log.info(prefix + "TimeInterceptor 进入 Controller 某个方法之前");
        request.setAttribute("startTime", new Date().getTime());

        try {
            String method = request.getMethod();
            HandlerMethod h = (HandlerMethod) hObject;
            log.info(prefix + "ReqMethod : {}", method);
            log.info(prefix + "URL : {}", request.getRequestURI());
            log.info(prefix + "QueryParam : {}", request.getQueryString());
            log.info(prefix + "Controller: {}", h.getBean().getClass().getName());
            log.info(prefix + "Method    : {}", h.getMethod().getName());

            if("GET".equals(method)){
                log.info(prefix + "postJson: {}", JSONObject.toJSONString(request.getParameterMap()));
            }else{
                CustomHttpServletRequestWrapper wrapper = new CustomHttpServletRequestWrapper(request);
                log.info(prefix + "postJson: {}", wrapper.getBody());
            }
        } catch (Exception e) {
            log.error(prefix + "记录日志失败" + e.getMessage());
        }
        return true;
    }
}
