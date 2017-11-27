package com.bau.rest.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by emrahsoytekin on 27.11.2017.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, authorization");
        httpServletResponse.addHeader("Access-Control-Max-Age", "60"); // seconds to cache preflight request --> less OPTIONS traffic
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, DELETE");
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        if ("OPTIONS".equalsIgnoreCase (httpServletRequest.getMethod ())){
            httpServletResponse.setStatus (HttpServletResponse.SC_OK);
        } else {
            chain.doFilter (request,response);

        }

    }

    @Override
    public void destroy() {

    }
}
