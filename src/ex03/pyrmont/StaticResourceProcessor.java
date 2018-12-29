package ex03.pyrmont;

import java.io.IOException;

import ex03.pyrmont.connector.http.HttpRequest;
import ex03.pyrmont.connector.http.HttpResponse;

/**
 * 用于处理静态请求资源
 * @author taojiajun
 *
 */
public class StaticResourceProcessor {
	public void process(HttpRequest request,HttpResponse response ){
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
