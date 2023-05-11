package com.zzx;

import com.alibaba.fastjson.JSON;
import com.zzx.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
@Component
public class LoginFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        // 1.获取本次请求的URI
        final String uri = req.getRequestURI();
        // log.info("请求成功,url为: {}",uri);
        // 2.如果本次路径不需要处理
        if(check(uri)){
        // log.info("本次请求{}不需要处理",uri);
            filterChain.doFilter(req,resp);
            return;
        }
        // 3.判断是否登录
        if(req.getSession().getAttribute("employee")!=null){
            log.info("用户已经登录,用户id为:{}",req.getSession().getAttribute("employee"));
            filterChain.doFilter(req,resp);
            return;
        }
        // 4.如果没有登录,通过输出流向客户端响应数据,跳转到登录界面
        log.info("没有登录,跳转到登录界面");
        resp.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }
    private boolean check(String uri){
        // 不需要处理的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        for (String url:urls) {
            boolean m = PATH_MATCHER.match(url,uri);
            if(m) return true;
        }
        return false;
    }
}
