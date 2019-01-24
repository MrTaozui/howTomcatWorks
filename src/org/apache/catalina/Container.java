package org.apache.catalina;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * tomcat 中使用得连接器  必须要实现这个接口
 * @author taojiajun
 *
 */
public interface Container {
/**
 * 在此方法中将request和response对象传给servlet容器
 * servlet容器会载入响应的servlet类，调用其service方法  管理session对象，记录错误消息等操作
 * @throws IOException
 * @throws ServletException
 */
	public void invoke() 
			throws IOException, ServletException;
}
