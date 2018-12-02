package ex02.pyrmont;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer1 {
	
	
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";//终止命令  比如关闭浏览器可以停止上传下载等功能
	
	private boolean shutdown = false;//是否接收到停止的命令
	  
	  public static void main(String[] args) {
		  HttpServer1 server=new HttpServer1();
		  server.await();
	}
	  public void await(){
		  
		  ServerSocket serverSocket=null;
		  int port=8080;
		  try {
			  //InetAddress.getByName("127.0.0.2") 绑定特定的地址  不是默认分配的
			  //serverSocket在不调用accept方法取出连接时，能接受的最大连接数  1
			serverSocket=new ServerSocket(port,1,InetAddress.getByName("localhost"));
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
			  request.parse();//处理请求
			  
			  //创建一个响应对象
			  Response response=new Response(output);
			  response.setRequest(request);//根据请求来进行操作
			  
			  //检查是否是一个静态请求还是一个servlet请求
			  if(request.getUri().startsWith("/servlet/")){
				  ServletProcessor1 processor=new ServletProcessor1();
				  processor.process(request, response);
			  }else{
				  StaticResourceProcessor processor=new StaticResourceProcessor();
				  processor.process(request, response);
			  }
			  
			  
			  //关闭网络连接
			  socket.close();
			  
			  //检查是否是关闭命令
			  shutdown=request.getUri().equals(SHUTDOWN_COMMAND);
			  
		} catch (IOException e) {			
			e.printStackTrace();
			System.exit(1);
		}
		  
	  }
	
	  }
	

}
