package ex04.pyrmont.core;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Container;
/**
 * Tomcat中的连接器 必须满足以下要求
 * 1、实现org.apache.catalina.Container 接口路
 * 2、负责创建实现了org.apache.catalina.Request接口的对象
 * 3、负责创建实现了org.apache.catalina.Response接口的对象
 * 
 * 创建request 和 response对象，然后调用invoke对象将两个对象
 * 交给servlet容器
 * @author taojiajun
 *
 */
public class SimpleContainer implements Container{

	@Override
	public void invoke() throws IOException, ServletException {
		
		
	}

}
