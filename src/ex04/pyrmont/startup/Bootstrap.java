/* explains Tomcat's default container */
package ex04.pyrmont.startup;

import ex04.pyrmont.core.SimpleContainer;
import org.apache.catalina.connector.http.HttpConnector;

public final class Bootstrap {
  public static void main(String[] args) {
    HttpConnector connector = new HttpConnector(); //  连接器
    SimpleContainer container = new SimpleContainer();
    connector.setContainer(container); // 设置启动的连接器
    try {
      connector.initialize();//初始化连接器
      connector.start();//

      // make the application wait until we press any key.
      System.in.read();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
/***
 * Tomcat连接器和 和servlet容器是相互独立的模块。   连接器是创建   request 和response  然后传给servlet容器
 */
