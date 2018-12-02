package ex02.pyrmont;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;

public class ServletProcessor2 {
	public void process(Request request,Response response){
		String uri=request.getUri();
		String servletName=uri.substring(uri.lastIndexOf("/")+1);
		/**URLClassLoader继承了ClassLoader，
		URLClassLoader提供了这个功能，它让我们可以通过以下几种方式进行加载：
	    * 文件: (从文件系统目录加载)
	    * jar包: (从Jar包进行加载)
	    * Http: (从远程的Http服务进行加载)
	    * **/
		URLClassLoader loader=null;
		try {
		      //创建一个url类加载器   只需要查找在webroot 下的目录就可以 
		      URL[] urls = new URL[1];
		      URLStreamHandler streamHandler = null;//防止 URL两个构造函数编译报错 指定参数类型
		      File classPath = new File(Constants.WEB_ROOT);
		      String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString() ;
		      //url 寻址方法
		      urls[0] = new URL(null, repository, streamHandler);
		      loader = new URLClassLoader(urls);
		    }
		    catch (IOException e) {
		      System.out.println(e.toString() );
		    }
		Class myClass = null;
		try {
			myClass = loader.loadClass(servletName);
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		}
		Servlet servlet = null;
		//要把reuqest 和response 暴露给使用者  防止调用其他方法 使用了不安全的操作
		RequestFacade requestFacade=new RequestFacade(request);
		ResponseFacade responseFacade=new ResponseFacade(response);
		
		try {
			servlet = (Servlet) myClass.newInstance();
			servlet.service(requestFacade, responseFacade);
		} catch (Exception e) {
			System.out.println(e.toString());
		} catch (Throwable e) {
			System.out.println(e.toString());
		}
	}

}
