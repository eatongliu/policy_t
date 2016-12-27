package com.gpdata.wanyou.base.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 
 * 基础 Filter 
 * 
 */
public class BaseFilter implements Filter {

    protected String getRequestContextPath(HttpServletRequest request) {
        
        StringBuilder buff = new StringBuilder();
        /* 当前可能的值是: (/dts) */
        String contextPath = request.getContextPath().intern();
        buff.append(contextPath);  
        if (!contextPath.endsWith("/")) {
            buff.append("/");
        }
        return buff.toString().intern();
        
    }
    
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	    // nothing
	}

	/**
	 * 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		
	    // nothing
	    
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// nothing
	}

}
