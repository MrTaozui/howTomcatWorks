/* explains Tomcat's default container */
package ex04.pyrmont.startup;

import ex04.pyrmont.core.SimpleContainer;
import org.apache.catalina.connector.http.HttpConnector;

public final class Bootstrap {
  public static void main(String[] args) {
    HttpConnector connector = new HttpConnector(); //  连接器
    SimpleContainer container = new SimpleContainer(); // 简单的servlet容器
    connector.setContainer(container); // 设置启动的连接器
    try {
      connector.initialize();//初始化连接器   创建一个ServerScoket  
      connector.start();//  connector启动线程  ServerScoket开始accept()阻塞状态  第一次创建    HttpProcessor，处于等待状态一个socket的提交，若有socket进来
      // 会解析scoket，构建HttpServletRequest  HttpServletResponse等。   等待的是HttpProcessor.assign(socket)  其中assign()和HttpProcessor的关系是
      //互相等待的的关系 
      
      
      // 然后会创建多个HttpProcessor 存在Stack(栈)中。
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
