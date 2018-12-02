package ex02.pyrmont;

import java.io.IOException;

/**
 * 用于处理静态请求资源
 * @author taojiajun
 *
 */
public class StaticResourceProcessor {
	public void process(Request request,Response response ){
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
