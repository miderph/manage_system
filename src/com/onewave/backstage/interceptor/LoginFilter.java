package com.onewave.backstage.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class LoginFilter implements Filter {
	private Logger logger = Logger.getLogger(LoginFilter.class);
	
	FilterConfig config; 

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		 this.config = null;  
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		Object user = request.getSession().getAttribute("user");
		String uri = request.getRequestURI();
		logger.info(uri);
		String header = request.getHeader("x-requested-with");//
		if(uri.indexOf("login.do")==-1){
			if(user == null){
				if (header != null && header.equalsIgnoreCase("XMLHttpRequest")) {
					// 如果是ajax请求响应头会有，x-requested-with；
					response.setHeader("_timeout", "true");// 在响应头设置session状态
					logger.info("timeout");
					return;
				}
			}
		}
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		 this.config = config;  

	}

}
