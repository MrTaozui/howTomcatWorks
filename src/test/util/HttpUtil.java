package test.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class HttpUtil {

    /**
     * get方式
     * 
     * @param url
     * @return
     */
    public static String http(String url, String ip, String port) {
        // 尝试发送请求
        URL u = null;
        HttpURLConnection con = null;
        try {
            System.out.println("send_url:" + url);
            u = new URL(url);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,Integer.parseInt(port)));
            con = (HttpURLConnection) u.openConnection(proxy);
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(url);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            // 一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * post方式
     * 
     * @param url
     * @param params
     * @return
     */
    public static String http(String url, Map<String, String> params, String ip, String port) {
        // 尝试发送请求
        URL u = null;
        HttpURLConnection con = null;
        try {
            // 构建请求参数
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                for (Entry<String, String> e : params.entrySet()) {
                    sb.append(e.getKey());
                    sb.append("=");
                    sb.append(e.getValue());
                    sb.append("&");
                }
                sb.substring(0, sb.length() - 1);
            }
            String s = sb.substring(0, sb.length() - 1);
            System.out.println("send_url:" + url);
            System.out.println("send_data:" + s);
            u = new URL(url);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,Integer.parseInt(port)));
            System.out.println("ip:" + ip+"port:"+port);
            con = (HttpURLConnection) u.openConnection(proxy);
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(s);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            // 一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
    
    
    /**
     * post方式
     * 
     * @param url
     * @param params
     * @return
     */
    public static String http(String url, String params, String ip, String port) {
        // 尝试发送请求
        URL u = null;
        HttpURLConnection con = null;
        try {
            // 构建请求参数
            /*StringBuffer sb = new StringBuffer();
            if (params != null) {
                for (Entry<String, Object> e : params.entrySet()) {
                    sb.append(e.getKey());
                    sb.append("=");
                    sb.append(e.getValue());
                    sb.append("&");
                }
                sb.substring(0, sb.length() - 1);
            }
            String s = sb.substring(0, sb.length() - 1);*/
        	System.out.println("send_url:" + url);
        	System.out.println("send_data:" + params);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,Integer.parseInt(port)));
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection(proxy);
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(params);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            // 一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
    
    
    public static String httpGet(String url, String ip, String port) {
        // 尝试发送请求
        URL u = null;
        HttpURLConnection con = null;
        try {
            System.out.println("send_url:" + url);
            u = new URL(url);
          //  Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,Integer.parseInt(port)));
           // con = (HttpURLConnection) u.openConnection(proxy);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(url);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            // 一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }
    
    public static String httpPost(String url, Map<String, String> params, String ip, String port) {
        // 尝试发送请求
        URL u = null;
        HttpURLConnection con = null;
        try {
            // 构建请求参数
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                for (Entry<String, String> e : params.entrySet()) {
                    sb.append(e.getKey());
                    sb.append("=");
                    sb.append(e.getValue());
                    sb.append("&");
                }
                sb.substring(0, sb.length() - 1);
            }
            String s = sb.substring(0, sb.length() - 1);
            System.out.println("send_url:" + url);
            System.out.println("send_data:" + s);
            u = new URL(url);
           // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,Integer.parseInt(port)));
           
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(s);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            // 一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
    
    
    public static void main(String[] args) {
    	Map<String, String> params = new HashMap<>();
    	params.put("name", "taojj");
    	params.put("age", "18");
	System.out.println(httpPost("http://localhost:8080/ModernServlet", params, "", ""));
//张三提交
	

	//李四提交


}
 
}
