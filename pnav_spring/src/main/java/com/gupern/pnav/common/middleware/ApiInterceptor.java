package com.gupern.pnav.common.middleware;

import com.alibaba.fastjson.JSONObject;
import com.gupern.pnav.common.bean.TokenValidator;
import com.gupern.pnav.common.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author: Gupern
 * @date: 2022/3/6 14:44
 * @description: API请求拦截器，为所有的请求打印日志，记录耗时
 */
@Order(2)
@Component
public class ApiInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ApiInterceptor.class);
    private static final String prefix = "[--ApiInterceptor log--] ";
    @Autowired
    private RedisUtil redisUtil;

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
     * @description: 打印request信息，并根据controller上的注解进行session校验
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hObject) throws IOException {
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
        // 根据controller上的注解进行session校验
        TokenValidator tokenValidator = null;
        if (hObject instanceof HandlerMethod) {
            tokenValidator = ((HandlerMethod) hObject).getMethodAnnotation(TokenValidator.class);
            if (tokenValidator == null) {
                // 如果没有该注解，则跳过
                return true;
            } else {
                // 如果有该注解，进行token校验
                String token = request.getHeader("token");
                log.info("token: {}", token);
                if (token==null) {
                    log.warn("token异常");
                    handleTokenFalseResponse(response);
                    return false;
                }
                // 拦截器在SpringContext初始化之前就执行了，Bean初始化之前它就执行了，所以它肯定是无法获取SpringIOC容器中的内容的。
                // https://blog.csdn.net/myITliveAAA/article/details/114241001
                if (redisUtil == null) {
                    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                    redisUtil = wac.getBean(RedisUtil.class);
                }
                // 1 从缓存中校验session是否存在
                if (redisUtil.get(token) != null) {
                    // 2 更新session有效时长 2min
                    redisUtil.set(token, redisUtil.get(token), 120L);
                    return true;
                } else {
                    log.warn("token异常");
                    handleTokenFalseResponse(response);
                    return false;
                }
            }
        }
        return true;
    }

    private void handleTokenFalseResponse(HttpServletResponse response) throws IOException {
        response.setStatus(400);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"code\":\"400\", \"失败原因\":\"token异常\"}");
        response.getWriter().flush();
    }

}
