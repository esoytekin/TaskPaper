package com.bau.rest.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleCorsFilter implements Filter {
	
	public static String ORIGIN = "Origin";
	
	

//	@Override
//	protected void doFilterInternal(HttpServletRequest request,
//			HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		System.out.println(request.getHeader(ORIGIN));
//		System.out.println(request.getMethod());
//		String origin = request.getHeader(ORIGIN);
//		response.setHeader("Access-Control-Allow-Origin", origin);//* or origin as u prefer
//		response.setHeader("Access-Control-Allow-Credentials", "true");
//		response.setHeader("Access-Control-Allow-Headers",
//				request.getHeader("Access-Control-Request-Headers"));
//		
//		filterChain.doFilter(request, response);
//		
////	    if (request.getMethod().equals("OPTIONS")) {
////	        try {
////	            response.getWriter().print("OK");
////	            response.getWriter().flush();
////	        } catch (IOException e) {
////	            e.printStackTrace();
////	        }
////	    }else{
////	    filterChain.doFilter(request, response);
////	    }	
//		
//	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		System.out.println(request.getHeader(ORIGIN));
		String origin = request.getHeader(ORIGIN);
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", origin);
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				request.getHeader("Access-Control-Request-Headers"));
		chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) {}

	public void destroy() {}

}