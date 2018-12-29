package ex03.pyrmont.connector.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.catalina.util.StringManager;

import ex03.pyrmont.ServletProcessor;
import ex03.pyrmont.StaticResourceProcessor;

//连接器   创建   解析请求
/* this class used to be called HttpServer */

/*    http请求示例：：：：：：
GET/sample.jspHTTP/1.1
Accept:image/gif.image/jpeg,* /*
Accept-Language:zh-cn
Connection:Keep-Alive
Host:localhost
User-Agent:Mozila/4.0(compatible;MSIE5.01;Window NT5.0)
Accept-Encoding:gzip,deflate
 
username=user&password=passworld*/




/**
* 
一个http请求的格式 如下所示：
<request-line>  :请求的类型，要访问的资源以及使用的HTTP的版本
<headers>   ：用来说明服务器要使用的附加信息
<blank line>   ： 空行
[<request-body>] ： 主体body 可以添加任意的数据

在HTTP请求中，第一行必须是一个请求行（request line），用来说明请求类型、要访问的资源以及使用的HTTP版本。
紧接着是一个首部（header）小节，
用来说明服务器要使用的附加信息。在首部之后是一个空行，再此之后可以添加任意的其他数据[称之为主体（body）]。
*/
public class HttpProcessor {
	//与此处理器关联的HTTP-连接器。
	private HttpConnector connector = null;
	private HttpRequest request;
	private HttpRequestLine requestLine = new HttpRequestLine();
	private HttpResponse response;
	
	protected String method = null;
	protected String queryString = null;
	
	//this string manager for this package.
	protected StringManager sm =
		StringManager.getManager("ex03.pyrmont.connector.http");
	
	public HttpProcessor(HttpConnector connector){
		this.connector = connector;
		
	}
	/**
	 * 完成4个操作：
	 * 1、创建一个HttpRequest对象
	 * 2、创建一个HttpResponse对象
	 * 3、解析请求的第一行内容和请求头信息，填充HttpRequest对象
	 * 4、将HttpRequest对象和HttpResponse对象传递给servletProcessor或StaticResourceProcessor
	 * 的process()方法。servletProcessor类会调用请求的servlet实例的service()方法，
	 * StaticResourceProcessor会将静态资源发送给客户端。
	 * @param socket
	 */
	public void process(Socket socket) {
		SocketInputStream input = null;
		OutputStream output = null;
		try {
			input = new SocketInputStream(socket.getInputStream(), 2048);
			output = socket.getOutputStream();
			//创建一个HttpRequest 对象并处理
			request = new HttpRequest(input);
			//创建一个HttpResponse 对象
			response = new HttpResponse(output);
			response.setRequest(request);
			//设置向客户端发送的Header 信息
			response.setHeader("Server", "Pyrmont Servlet Container");
			
			parseRequest(request,response);
			parseHeaders(input);
			
			//检查一个i请求是一个servlet 还是一个静态的资源
			//servlet请求以"/servlet/"开头
			if(request.getRequestURI().startsWith("/servlet/")){
				ServletProcessor processor =new ServletProcessor();
				processor.process(request, response);
			}else{
				StaticResourceProcessor processor = new StaticResourceProcessor();
				processor.process(request, response);
			}
			//关闭socket
			socket.close();
			//这个应用没有shutdown
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
