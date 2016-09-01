package com.test.web.filter;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 解决中文乱码的字符过滤器
 */
public class CharacterEncodingFilter implements Filter{

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		ServletRequest proxy = getHttpServletRequestProxy(request);
		
		chain.doFilter(proxy, response);
		
	}

	
	private ServletRequest getHttpServletRequestProxy(final HttpServletRequest request){
		ServletRequest proxy = (ServletRequest) Proxy.newProxyInstance(CharacterEncodingFilter.class.getClassLoader(),
					request.getClass().getInterfaces(), new InvocationHandler() {
						
						@Override
						public Object invoke(Object proxy, Method method, Object[] args)
								throws Throwable {
							if(request.getMethod().equalsIgnoreCase("get") && method.getName().equals("getParameter")){
								String value = (String) method.invoke(request, args);
								if(value == null){
									return null;
								}
								
								return new String(value.getBytes("ISO-8859-1"),"UTF-8");
							}else{
								return method.invoke(request, args);
							}
						}
					});
		
		return proxy;
	}
}
