package com.test.common.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 压缩过滤器，将web应用中的文本都经过压缩后再输出到浏览器
 * @author Administrator
 *
 */
public class GzipFilter implements Filter{

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest)req;
		final HttpServletResponse response = (HttpServletResponse) resp;
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(bout));
		
		chain.doFilter(request, getProxyResponse(response, bout, pw));
		pw.close();
		
		byte[] result = bout.toByteArray();
		
		ByteArrayOutputStream b2 = new ByteArrayOutputStream();
		GZIPOutputStream gzipout = new GZIPOutputStream(b2);
		gzipout.write(result);
		gzipout.close();
		
		byte[] result2 = b2.toByteArray();
		
		response.setHeader("content-encoding", "gzip");
		response.setContentLength(result2.length);
		response.getOutputStream().write(result2);
		
		
	}

	private ServletResponse getProxyResponse(final HttpServletResponse response,
			final ByteArrayOutputStream bout, 
			final PrintWriter pw){
		return (ServletResponse)Proxy.newProxyInstance(GzipFilter.class.getClassLoader(),
				response.getClass().getInterfaces(), new InvocationHandler() {
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						if("getWriter".equals(method.getName())){
							return pw;
						}else if("getOutputStream".equals(method.getName())){
							return new MyServletOutputStream(bout);
						}else{
							return method.invoke(response, args);
						}
					}
				});
	}
}

class MyServletOutputStream extends ServletOutputStream{

	private ByteArrayOutputStream buf = null;
	
	public MyServletOutputStream(ByteArrayOutputStream buf){
		this.buf = buf;
	}

	@Override
	public void write(int b) throws IOException {
		buf.write(b);
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {}

}
