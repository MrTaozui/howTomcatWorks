package ex03.pyrmont.connector.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.util.ParameterMap;
import org.apache.catalina.util.RequestUtil;

public class HttpRequest implements HttpServletRequest{
	
	private String contentType;
	private int contentLength;
	private InetAddress inetAddress;
	private InputStream input;
	private String method;
	private String protocol;
	private String queryString;
	private String requeURI;
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
			      RequestUtil.parseParameters(results, queryString, encoding);
			    }
			    catch (UnsupportedEncodingException e) {
			      ;
			    }
		   
		   
		   
		   
		   
	  }
	  
	 
	/***************implements HttpServletRequest*********/
	public Object getAttribute(String s) {
		
		return null;
	}

	public Enumeration getAttributeNames() {
		
		return null;
	}

	public String getCharacterEncoding() {
		
		return null;
	}

	public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
		
		
	}

	public int getContentLength() {
		
		return 0;
	}

	public String getContentType() {
		
		return null;
	}

	public ServletInputStream getInputStream() throws IOException {
		
		return null;
	}

	public String getParameter(String s) {
		
		return null;
	}

	public Enumeration getParameterNames() {
		
		return null;
	}

	public String[] getParameterValues(String s) {
		
		return null;
	}

	public Map getParameterMap() {
		
		return null;
	}

	public String getProtocol() {
		
		return null;
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
		
		return null;
	}

	public String getRemoteAddr() {
		
		return null;
	}

	public String getRemoteHost() {
		
		return null;
	}

	public void setAttribute(String s, Object obj) {
		
		
	}

	public void removeAttribute(String s) {
		
		
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

	public RequestDispatcher getRequestDispatcher(String s) {
		
		return null;
	}

	public String getRealPath(String s) {
		
		return null;
	}

	public String getAuthType() {
		
		return null;
	}

	public Cookie[] getCookies() {
		
		return null;
	}

	public long getDateHeader(String s) {
		
		return 0;
	}

	public String getHeader(String s) {
		
		return null;
	}

	public Enumeration getHeaders(String s) {
		
		return null;
	}

	public Enumeration getHeaderNames() {
		
		return null;
	}

	public int getIntHeader(String s) {
		
		return 0;
	}

	public String getMethod() {
		
		return null;
	}

	public String getPathInfo() {
		
		return null;
	}

	public String getPathTranslated() {
		
		return null;
	}

	public String getContextPath() {
		
		return null;
	}

	public String getQueryString() {
		
		return queryString;
	}

	public String getRemoteUser() {
		
		return null;
	}

	public boolean isUserInRole(String s) {
		
		return false;
	}

	public Principal getUserPrincipal() {
		
		return null;
	}

	public String getRequestedSessionId() {
		
		return null;
	}

	public String getRequestURI() {
		
		return null;
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
		
		return false;
	}
	
	
	/*****************implements HttpServletRequest end****************/

}
