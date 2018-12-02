package ex02.pyrmont;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class Response implements ServletResponse{
	
	private static final int BUFFER_SIZE = 1024;
	Request request;//持有客户端请求
	OutputStream output;//打印流
	PrintWriter writer;

	public Response(OutputStream output){
		this.output=output;
	}
	
	public void setRequest(Request request){
		this.request=request;
	}
	
	public void sendStaticResource() throws IOException{
		byte[] bytes=new byte[BUFFER_SIZE];
		FileInputStream fis=null;
		try {
		File file=new File(Constants.WEB_ROOT,request.getUri());
		if(file.exists()){			
				fis=new FileInputStream(file);
				int ch=fis.read(bytes,0,BUFFER_SIZE);
				while(ch!=-1){
					output.write(bytes, 0, ch);
					ch = fis.read(bytes, 0, BUFFER_SIZE);//读完之后的下一次再读  就会返回 -1
				}			
		}else{
			//没有找到文件
			String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
			          "Content-Type: text/html\r\n" +
			          "Content-Length: 23\r\n" +
			          "\r\n" +
			          "<h1>File Not Found</h1>";
			this.output.write(errorMessage.getBytes());
			
		}
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		}finally{
			if(fis!=null)
				fis.close();
		}
	}
	/*****************implements ServletResponse****************/

	public void flushBuffer() throws IOException {
		
		
	}

	public int getBufferSize() {
		
		return 0;
	}

	public String getCharacterEncoding() {
		
		return null;
	}

	public Locale getLocale() {
		
		return null;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		
		return null;
	}

	public PrintWriter getWriter() throws IOException {
		//自动刷新  println()会自动刷新 但是 print() 不会自动刷新writer.println(x); writer.print(s);
		
		this.writer=new PrintWriter(output, true);
		
		return this.writer;
	}

	public boolean isCommitted() {
		
		return false;
	}

	public void reset() {
		
		
	}

	public void resetBuffer() {
		
		
	}

	public void setBufferSize(int arg0) {
		
		
	}

	public void setContentLength(int arg0) {
		
		
	}

	public void setContentType(String arg0) {
		
		
	}

	public void setLocale(Locale arg0) {
		
		
	}
 /**************implements ServletResponse end*******************/
	
	
}
