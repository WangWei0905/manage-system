
package com.jala.architect.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义拦截器
 * @author ww
 * @date 2017/7/26.
 */
//@Component
public class MyWebFilter implements Filter {
    private Log log = LogFactory.getLog(MyWebFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        log.info("==>拦截请求"+response.getStatus());
log.info(((HttpServletRequest) req).getRequestURL());
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
