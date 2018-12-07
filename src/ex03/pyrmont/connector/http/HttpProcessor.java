package ex03.pyrmont.connector.http;
//连接器   创建
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
	private Http
	
	public HttpProcessor(HttpConnector connector){
		this.
		
	}
	

}
