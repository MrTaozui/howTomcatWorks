package ex01.pyrmont;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	
	public static final String WEB_ROOT=System.getProperty("user.dir")+File.separator+"webroot";//资源路径
	
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";//终止命令  比如关闭浏览器可以停止上传下载等功能
	
	private boolean shutdown = false;//是否接收到停止的命令
	  
	  public static void main(String[] args) {
		  HttpServer server=new HttpServer();
		  server.await();
	}
	  public void await(){
		  
		  ServerSocket serverSocket=null;
		  int port=8080;
		  try {
			  //InetAddress.getByName("127.0.0.2") 绑定特定的地址  不是默认分配的
			  //serverSocket在不调用accept方法取出连接时，能接受的最大连接数  1
			serverSocket=new ServerSocket(port,1,InetAddress.getByName("127.0.0.2"));
		}  catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		  while (!shutdown) {//循环等待请求
			  InputStream input=null;
			  OutputStream output=null;
			  Socket socket=null;
		  try {
			  socket=serverSocket.accept();
			  input=socket.getInputStream();
			  output=socket.getOutputStream();
			
			  //创建Request 对象并处理
			  Request request=new Request(input);
			  request.parse();
			  
			  
			  
			  
					  
		} catch (IOException e) {			
			e.printStackTrace();
			continue;
		}
		  
	  }
	
	  }
	

}
