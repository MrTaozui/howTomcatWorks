package ex03.pyrmont.connector.http;
import java.io.File;

/**
 * 常量
 * @author taojiajun
 *
 */
public final class Constants {
  public static final String WEB_ROOT =
    System.getProperty("user.dir") + File.separator  + "webroot";//资源文件的目录
  public static final String Package = "ex03.pyrmont.connector.http";//属性文件包的位置
  public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;//连接超时的时间
  public static final int PROCESSOR_IDLE = 0;
  public static final int PROCESSOR_ACTIVE = 1;
}
