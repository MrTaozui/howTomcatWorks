package ex03.pyrmont.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 连接器：
 * 工作
 * 1.等待HTTP请求
 * 2.为每个请求创建个HttpProcessor实例
 * 3.调用HttpProcessor的process方法
 * @author taojiajun
 *
 */
public class HttpConnector implements Runnable{
	
	boolean stopped;
	private String scheme = "http";
	
	public String getScheme() {
	    return scheme;
	  }
	public void run() {
		ServerSocket serverSocket = null;
		 int port = 8080;
		    try {
		      serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		    }
		    catch (IOException e) {
		      e.printStackTrace();
		      System.exit(1);
		    }
		    while(!stopped){
		    	Socket socket = null;
		        try {
		          socket = serverSocket.accept();
		        }
		        catch (Exception e) {
		          continue;
		        }
		        //提交一个 socket 给处理器
		        HttpProcessor processor = new HttpProcessor(this);
		        processor.process(socket);
		    }
		
	}
	public void start() {
	    Thread thread = new Thread(this);
	    thread.start();
	  }

}
