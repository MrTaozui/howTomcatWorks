package ex03.pyrmont.connector.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.util.Enumerator;
import org.apache.catalina.util.ParameterMap;
import org.apache.catalina.util.RequestUtil;

import ex03.pyrmont.connector.RequestStream;
/**
 * 将要做的工作：
 * 1、读取套接字的输入流
 * 2、解析请求行
 * 3、解析请求头
 * 4、解析cookie
 * 5、获取参数
 */
public class HttpRequest implements HttpServletRequest{
	
	private String contentType;
	private int contentLength;
	private InetAddress inetAddress;
	private InputStream input;
	private String method;
	private String protocol;
	private String queryString;
	private String requestURI;
	private String serverName;
	private int serverPort;
	private Socket socket;
	private boolean requestedSessionCookie;
	private String requestedSessionId;
	private boolean requestedSessionURL;
	
	protected HashMap attributes = new HashMap();//请求的属性值
	
	protected String authorization = null;//随此请求发送的授权凭据
	
	protected String contextPath = "";//此请求的上下文路径
	
	protected ArrayList cookies = new ArrayList();//与此请求关联的一组cookie
	
	protected static ArrayList empty = new ArrayList();//用于返回空枚举的空集合。不为这个集合添加任何元素!
	//在getDateHeader()中使用的SimpleDateFormat格式集。
	protected SimpleDateFormat formats[] = {
			new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
		    new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
		    new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
	};
	
	//与此请求关联的HTTP标头，按名称键控,的值是对应头值的数组列表。
	protected HashMap headers = new HashMap();
	//此请求的解析参数。只有当参数信息是通过getParameter()家族的方法调用。
	protected ParameterMap parameters = null;
	//请求参数是否被编译过
	protected boolean parsed = false;
	protected String pathInfo = null;
	//由getReader返回的读取器，如果有的话
	protected BufferedReader reader = null;
	//由getInputStream()(如果有的话)返回的ServletInputStream。
	protected ServletInputStream stream = null;
	
	
	  public HttpRequest(InputStream input) {
		    this.input = input;
		  }

	  /**
	   * 设置请求头参数
	   * @param name
	   * @param value
	   */
	  public void addHeader(String name, String value){
		  name = name .toLowerCase();
		  synchronized(headers){
			  ArrayList values=(ArrayList)headers.get(name);
			  if(values == null){
				  values = new ArrayList();
				  headers.put(name, values);
			  }
			  values.add(values);
		  }
	  }
	  /**
	   * 如果该请求尚未发生，则解析该请求的参数。
	   * 如果参数同时出现在查询字符串和请求中内容，它们被合并。
	   */
	  protected void parseParameters() {
		  if (parsed)
			  return;
		  ParameterMap results = parameters;
		  if (results == null)
			  results = new ParameterMap();
		  results.setLocked(false);
		  String encoding = this.getCharacterEncoding();
		  if (encoding == null)
			  encoding = "ISO-8859-1";
		//解析查询字符串中指定的任何参数
		  String queryString = this.getQueryString();
		   try {
			      RequestUtil.parseParameters(results, queryString, encoding);//queryString 的解析
			    }
			    catch (UnsupportedEncodingException e) {
			      ;
			    }
		   String contentType = getContentType();// 举例：text/html;charset:utf-8;
		   if(contentType == null)
			   contentType = "";
		   int semicolon = contentType.indexOf(';');
		   if (semicolon >= 0) {
			      contentType = contentType.substring(0, semicolon).trim();
			   }
		    else {
		        contentType = contentType.trim();
		      }
		    if ("POST".equals(getMethod()) && (getContentLength() > 0)
		    	      && "application/x-www-form-urlencoded".equals(contentType)){//表单默认的提交数据的格式
		    	try {
		    	int max = getContentLength();
		    	int len = 0;
		    	byte buf[] = new byte[getContentLength()];
		    	ServletInputStream is = getInputStream();
		    	while(len < max){
		    		int next = is.read(buf, len, max - len);
		    		if (next < 0) {
		    			break;
		    		}
		    		len += next;
		    	}
		    	is.close();
		        if (len < max) {
		            throw new RuntimeException("Content length mismatch");
		          }
		        RequestUtil.parseParameters(results, buf, encoding);// 表单等的提交
				} catch (UnsupportedEncodingException ue) {
					;
				}
		    	catch (IOException e) {
			        throw new RuntimeException("Content read fail");
			      }
		    }
			  //存储最终的结果
		    results.setLocked(true);//已被锁定
		    parsed = true;//
		    parameters = results;
		   
	  }
	  public void addCookie(Cookie cookie) {
		  synchronized (cookies) {
		      cookies.add(cookie);}
	  }
	  
	  public ServletInputStream createInputStream()  throws IOException {
		  return (new RequestStream(this));
	  }
	  
	  public InputStream getStream() {
		    return this.input;
		  }
	  
	  public void setContentLength(int length) {
		    this.contentLength = length;
		  }
	  
	  public void setContentType(String type) {
		    this.contentType = type;
		  }
	  
	  public void setInet(InetAddress inetAddress) {
		    this.inetAddress = inetAddress;
		  }

	  public void setContextPath(String path) {
		    if (path == null)
		      this.contextPath = "";
		    else
		      this.contextPath = path;
		  }  
	 
	  public void setMethod(String method) {
		    this.method = method;
		  }
	  
	  public void setPathInfo(String path) {
		    this.pathInfo = path;
		  }

	  public void setProtocol(String protocol) {
		    this.protocol = protocol;
		  }


	  public void setQueryString(String queryString) {
		    this.queryString = queryString;
		  }
	  
	  public void setRequestURI(String requestURI) {
		    this.requestURI = requestURI;
		  }
	  //设置服务器(虚拟主机)的名称来处理此请求。
	  public void setServerName(String name) {
		    this.serverName = name;
		  }
	  //设置服务器端口号来处理此请求。
	  public void setServerPort(int port) {
		    this.serverPort = port;
		  }
	  
	  public void setSocket(Socket socket) {
		    this.socket = socket;
		  }
/**
 * 设置一个标志，指示此请求的请求会话ID是否通过cookie传入。这通常由http连接器在解析请求头时调用。
 */
	  public void setRequestedSessionCookie(boolean flag) {
		    this.requestedSessionCookie = flag;
		  }	  
	  
	  public void setRequestedSessionId(String requestedSessionId) {
		    this.requestedSessionId = requestedSessionId;
		  }

	  public void setRequestedSessionURL(boolean flag) {
		    requestedSessionURL = flag;
		  }
	  
	  
	/***************implements HttpServletRequest*********/
	public Object getAttribute(String name) {
	    synchronized (attributes) {
	        return (attributes.get(name));
	      }
	}

	public Enumeration getAttributeNames() {
		   synchronized (attributes) {
			      return (new Enumerator(attributes.keySet()));
			    }
	}

	public String getCharacterEncoding() {
		
		return null;
	}

	public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
		
		
	}

	public int getContentLength() {
		
		return contentLength;
	}

	public String getContentType() {
		
		return contentType;
	}

	public ServletInputStream getInputStream() throws IOException {
		if (reader != null)
			throw new IllegalStateException("getInputStream has been called");
		if (stream == null)
			stream = this.createInputStream();
		return stream;
	}

	public String getParameter(String name) {
		parseParameters();
		String values[] = (String[]) parameters.get(name);
		if (values != null)
		      return (values[0]);
		    else
		      return (null);
	}

	public Enumeration getParameterNames() {
	    parseParameters();
	    return (new Enumerator(parameters.keySet()));
	}

	public String[] getParameterValues(String name) {
	    parseParameters();
	    String values[] = (String[]) parameters.get(name);
	    if (values != null)
	        return (values);
	    else
	        return null;
	}

	public Map getParameterMap() {
	    parseParameters();
	    return (this.parameters);
	}

	public String getProtocol() {
		
		return protocol;
	}

	public String getScheme() {
		
		return null;
	}

	public String getServerName() {
		
		return null;
	}

	public int getServerPort() {
		
		return 0;
	}

	public BufferedReader getReader() throws IOException {
	    if (stream != null)
	        throw new IllegalStateException("getInputStream has been called.");
	    if (reader == null) {
	        String encoding = getCharacterEncoding();
	        if (encoding == null)
	          encoding = "ISO-8859-1";
	        InputStreamReader isr =
	          new InputStreamReader(createInputStream(), encoding);
	          reader = new BufferedReader(isr);
	      }
		return reader;
	}

	public String getRemoteAddr() {
		
		return null;
	}

	public String getRemoteHost() {
		
		return null;
	}

	public void setAttribute(String key, Object value) {
		
		
	}

	public void removeAttribute(String attribute) {
		
		
	}

	public Locale getLocale() {
		
		return null;
	}

	public Enumeration getLocales() {
		
		return null;
	}

	public boolean isSecure() {
		
		return false;
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		
		return null;
	}

	public String getRealPath(String path) {
		
		return null;
	}

	public String getAuthType() {
		
		return null;
	}

	public Cookie[] getCookies() {
		 synchronized (cookies) {
		      if (cookies.size() < 1)
		        return (null);
		      Cookie results[] = new Cookie[cookies.size()];
		      return ((Cookie[]) cookies.toArray(results));
		    }
	}

	public long getDateHeader(String name) {
	    String value = getHeader(name);
	    if (value == null)
	        return (-1L);
	    value += " ";
	    for (int i = 0; i < formats.length; i++) {
	        try {
	          Date date = formats[i].parse(value);
	          return (date.getTime());
	        }
	        catch (ParseException e) {
	          ;
	        }
	      }
	      throw new IllegalArgumentException(value);		
	}

	public String getHeader(String name) {
	    name = name.toLowerCase();
	    synchronized (headers) {
	        ArrayList values = (ArrayList) headers.get(name);
	        if (values != null)
	          return ((String) values.get(0));
	        else
	          return null;
	      }
	}

	public Enumeration getHeaders(String name) {
	    name = name.toLowerCase();
	    synchronized (headers) {
	        ArrayList values = (ArrayList) headers.get(name);
	        if (values != null)
	          return (new Enumerator(values));
	        else
	          return (new Enumerator(empty));
	      }	    
	}

	public Enumeration getHeaderNames() {
	    synchronized (headers) {
	        return (new Enumerator(headers.keySet()));
	      }
	}

	public int getIntHeader(String name) {
	    String value = getHeader(name);
	    if (value == null)
	      return (-1);
	    else
	      return (Integer.parseInt(value));
	}

	public String getMethod() {
		
		return method;
	}

	public String getPathInfo() {
		
		return pathInfo;
	}

	public String getPathTranslated() {
		
		return null;
	}

	public String getContextPath() {
		
		return contextPath;
	}

	public String getQueryString() {
		
		return queryString;
	}

	public String getRemoteUser() {
		
		return null;
	}

	public boolean isUserInRole(String role) {
		
		return false;
	}

	public Principal getUserPrincipal() {
		
		return null;
	}

	public String getRequestedSessionId() {
		
		return null;
	}

	public String getRequestURI() {
		
		return requestURI;
	}

	public StringBuffer getRequestURL() {
		
		return null;
	}

	public String getServletPath() {
		
		return null;
	}

	public HttpSession getSession(boolean flag) {
		
		return null;
	}

	public HttpSession getSession() {
		
		return null;
	}

	public boolean isRequestedSessionIdValid() {
		
		return false;
	}

	public boolean isRequestedSessionIdFromCookie() {
		
		return false;
	}

	public boolean isRequestedSessionIdFromURL() {
		
		return false;
	}

	public boolean isRequestedSessionIdFromUrl() {
		
		 return isRequestedSessionIdFromURL();
	}
	
	
	/*****************implements HttpServletRequest end****************/

}
