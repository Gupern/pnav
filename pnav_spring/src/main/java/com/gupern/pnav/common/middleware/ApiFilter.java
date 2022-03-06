package com.gupern.pnav.common.middleware;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author: Gupern
 * @date: 2022/3/6 15:39
 * @description: 过滤器，需要用到，否则request不能往下传, 优先级比拦截器高
 * 参考链接：https://www.cnblogs.com/chengxy-nds/p/13042013.html
 */

@Order(1)
@Component // 需要加这个注解，否则过滤器不生效
@WebFilter(filterName = "ApiFilter",urlPatterns = {"/**"})
public class ApiFilter  implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new CustomHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if (null == requestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }

    @Override
    public void destroy() {
    }
}
