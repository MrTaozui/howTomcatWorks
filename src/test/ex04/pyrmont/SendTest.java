package test.ex04.pyrmont;

import test.util.HttpUtil;

public class SendTest implements Runnable{

	@Override
	public void run() {
		HttpUtil.httpGet("http://localhost:8080/servlet/ModernServlet?userName=tarzan&password=pwd","","");
		
	}

}
