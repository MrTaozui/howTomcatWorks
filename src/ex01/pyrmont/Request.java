package ex01.pyrmont;

import java.io.IOException;
import java.io.InputStream;

public class Request {
	
	private InputStream input;
	private String uri;
	
	public Request(InputStream input){
		this.input=input;
	}
	public void parse(){
		StringBuffer request=new StringBuffer();
		int i;
		byte[] buffer=new byte[2048];//缓冲区读入数据  一次读不完 读入字节数组中 做缓冲
		try {
			i = input.read(buffer);//传过来的二进制转为  字符
		} catch (IOException e) {
			e.printStackTrace();
			i = -1;
		}
		for(int j=0;j<i;j++){
			request.append((char)buffer[j]);
		}
		System.out.println(" 请求资源为："+request.toString());
		this.uri = parseUri(request.toString());
	}
	private String parseUri(String requestString) {
	    int index1, index2;
	    index1 = requestString.indexOf(' ');
	    if (index1 != -1) {
	      index2 = requestString.indexOf(' ', index1 + 1);
	      if (index2 > index1)
	        return requestString.substring(index1 + 1, index2);
	    }
	    return null;
	  }
	public String getUri() {
		return this.uri;
	}
	 public static void main(String[] args) {
			String url= "GET /lalala HTTP/1.1";//http 请求的第一行
			System.out.println(new Request(null).parseUri(url));
		}
}
