package ex05.pyrmont.startup;

import org.apache.catalina.Loader;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.http.HttpConnector;

import ex05.pyrmont.core.SimpleLoader;
import ex05.pyrmont.core.SimpleWrapper;
import ex05.pyrmont.valves.ClientIPLoggerValve;
import ex05.pyrmont.valves.HeaderLoggerValve;

public class Bootstrap1 {

	public static void main(String[] args) {
		/**
		 * 只是调用了http://localhost:8080/ModernServlet，没有调用其他的servlet
		 */
		HttpConnector connector = new HttpConnector();
	    Wrapper wrapper = new SimpleWrapper();
	    wrapper.setServletClass("ModernServlet");// 设置要加载的servlet的名字
	    Loader loader = new SimpleLoader();//加载器
	    Valve valve1 = new HeaderLoggerValve();
	    Valve valve2 = new ClientIPLoggerValve();

	    wrapper.setLoader(loader);
	    ((Pipeline) wrapper).addValve(valve1);
	    ((Pipeline) wrapper).addValve(valve2);

	    connector.setContainer(wrapper); //设置servlet容器

	    try {
	      connector.initialize();//初始化工作 创建ServeSocket
	      connector.start();// 开始监听客户端的sockt请求，多线程处理，分派socket，解析sockt等等

	      // make the application wait until we press a key.
	      System.in.read();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}
}
