package ex03.pyrmont.startup;

import ex03.pyrmont.connector.http.HttpConnector;

/**
 * 启动程序引导类
 * @author taojiajun
 *
 */
public class Bootstrap {
	
	public static void main(String[] args) {
		HttpConnector connector=new HttpConnector();//创建一个连接器
		connector.start();//启动连接器
	}
	
}
