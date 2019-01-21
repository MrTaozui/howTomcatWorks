package ex03.pyrmont.connector.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.sql.rowset.serial.SerialException;

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
	
	// 来管理和发送错误消息
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
			
			parseRequest(input,output);//解析请求
			parseHeaders(input);//解析请求
			
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
	/**
	 * 帮助填充request对象
	 * @param input
	 */
	private void parseHeaders(SocketInputStream input) {
		TODO
		//TODO 3.3.3
		
	}
	/**
	 * 帮助填充request对象
	 * @param request2
	 * @param response2
	 * @throws IOException 
	 * @throws ServletException 
	 * @throws SerialException 
	 */
	private void parseRequest(SocketInputStream input, OutputStream output) throws IOException, ServletException {
		
		input.readRequestLine(requestLine);// 读取请求的第一行 处理的结果放入requestLine中
		String method = new String(requestLine.method, 0, requestLine.methodEnd);//请求方法		
		String uri = null; //请求资源
		String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);//http 版本
		//校验请求第一行
		if(method.length() < 1){
			throw new ServletException("Missing HTTP request method");
		}else if (requestLine.uriEnd < 1){
			throw new ServletException("Missing HTTP request URI");
		}
		//解析uri中的查询参数
		int question = requestLine.indexOf("?");
		if(question >= 0){
			request.setQueryString(new String(requestLine.uri,question+1,
					requestLine.uriEnd - question -1));
			uri  = new String (requestLine.uri, 0, requestLine.uriEnd);
		}else{
			request.setQueryString(null);
			uri  = new String (requestLine.uri, 0, requestLine.uriEnd);
		}
		// 相对路径 如  /myApp/Modernservlet?name=xxx
		//校验http协议中的绝对路径uri 如 http://www.brainysoftware.com/inde.html?name=xxx
		//绝对路径检查
		if(!uri.startsWith("/")){
			int pos = uri.indexOf("://");
			if(pos != -1){
				pos = uri.indexOf("/", pos + 3);
				if(pos == -1){
					uri = "";
				}else {
					uri = uri.substring(pos);
				}
			}
		}
		// 处理请求中的 session ID
		String match = ";jsessionid=";
		int semicolon = uri.indexOf(match);
		if(semicolon >= 0 ){
			String rest = uri.substring(semicolon +match.length());
			int semicolon2 = rest.indexOf(";");
			if(semicolon2 >= 0 ){
				request.setRequestedSessionId(rest.substring(0, semicolon2));
				rest = rest.substring(semicolon2);
			}
			else{
				request.setRequestedSessionId(rest);
				rest = "";
			}
			request.setRequestedSessionURL(true);
			uri = uri.substring(0, semicolon)+rest;
		}
		else{
			request.setRequestedSessionId(null);
			request.setRequestedSessionURL(false);
		}
		//正常化uri
		String normalizedUri = normalize(uri);
		request.setMethod(method);
		request.setProtocol(protocol);
		if(normalizedUri != null){
			request.setRequestURI(normalizedUri);
		}else{
			request.setRequestURI(uri);
		}
		if(normalizedUri == null){
			throw new ServletException("Invalid URI:"+ uri +"'");
		}

	}
	/**
	 * 对非正常的uri 进行修正，返回相同的uri或者修正后的uri  无法修正的uri返回null 为无效的
	 * 帮助填充request对象
	 */
	 protected String normalize(String path) {
		    if (path == null)
		      return null;
		    // Create a place for the normalized path
		    String normalized = path;

		    // Normalize "/%7E" and "/%7e" at the beginning to "/~"
		    //   /%7E" and "/%7e" 转成/~
		    if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
		      normalized = "/~" + normalized.substring(4);

		    // Prevent encoding '%', '/', '.' and '\', which are special reserved
		    // characters
		    if ((normalized.indexOf("%25") >= 0)
		      || (normalized.indexOf("%2F") >= 0)
		      || (normalized.indexOf("%2E") >= 0)
		      || (normalized.indexOf("%5C") >= 0)
		      || (normalized.indexOf("%2f") >= 0)
		      || (normalized.indexOf("%2e") >= 0)
		      || (normalized.indexOf("%5c") >= 0)) {
		      return null;
		    }

		    if (normalized.equals("/."))
		      return "/";

		    // Normalize the slashes and add leading slash if necessary
		    if (normalized.indexOf('\\') >= 0)
		      normalized = normalized.replace('\\', '/');
		    if (!normalized.startsWith("/"))
		      normalized = "/" + normalized;

		    // Resolve occurrences of "//" in the normalized path
		    while (true) {
		      int index = normalized.indexOf("//");
		      if (index < 0)
		        break;
		      normalized = normalized.substring(0, index) +
		        normalized.substring(index + 1);
		    }

		    // Resolve occurrences of "/./" in the normalized path
		    while (true) {
		      int index = normalized.indexOf("/./");
		      if (index < 0)
		        break;
		      normalized = normalized.substring(0, index) +
		        normalized.substring(index + 2);
		    }

		    // Resolve occurrences of "/../" in the normalized path
		    while (true) {
		      int index = normalized.indexOf("/../");
		      if (index < 0)
		        break;
		      if (index == 0)
		        return (null);  // Trying to go outside our context
		      int index2 = normalized.lastIndexOf('/', index - 1);
		      normalized = normalized.substring(0, index2) +
		        normalized.substring(index + 3);
		    }

		    // Declare occurrences of "/..." (three or more dots) to be invalid
		    // (on some Windows platforms this walks the directory tree!!!)
		    if (normalized.indexOf("/...") >= 0)
		      return (null);

		    // Return the normalized path that we have completed
		    return (normalized);

		  }

	

}
