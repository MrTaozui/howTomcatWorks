package org.apache.catalina.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.Cookie;

/**
 * 通用的请求解析和编码实用程序方法。
 *
 */
public final class RequestUtil{
	
	//于在cookie中生成可读日期的DateFormat。
	private static SimpleDateFormat format=new SimpleDateFormat(" EEEE, dd-MMM-yy kk:mm:ss zz");
	//设置时区
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    //按照RFC 2109对cookie进行编码。可以使用生成的字符串作为Set-Cookie报头的值。
    public static String encodeCookie(Cookie cookie){
    	
    	StringBuffer buf = new StringBuffer(cookie.getName());// cookie.getName 取得Cookie的名字 一个cookie 就是一个键值对
    	buf.append("=");
    	buf.append(cookie.getValue());
    	
    	if(cookie.getComment() != null){//cookie.getComment()  返回的是cookie的注释
    		buf.append("; Comment=\"");
    		buf.append(cookie.getComment());
    		buf.append("\"");
    	}
        if (cookie.getDomain() != null) {//cookie.getDomain() 返回的是 获取cookie的有效域
            buf.append("; Domain=\"");
            buf.append(cookie.getDomain());
            buf.append("\"");
        }
        long age =  cookie.getMaxAge();//cookie.getMaxAge() 获取Cookies的有效期
        if (cookie.getMaxAge() >= 0) {
            buf.append("; Max-Age=\"");
            buf.append(cookie.getMaxAge());
            buf.append("\"");
        } 
        /**
         * 设置cookie的有效路径，比如把cookie的有效路径设置为"/xdp"，那么浏览器访问"xdp"目录下的web资源时，
         * 都会带上cookie，再比如把cookie的有效路径设置为"/xdp/gacl"，
         * 那么浏览器只有在访问"xdp"目录下的"gacl"这个目录里面的web资源时才会带上cookie一起访问，而当访问"xdp"目录下的web资源时，浏览器是不带cookie的
         */
        if (cookie.getPath() != null) {//cookie.getPath()该方法设置 cookie 适用的域，例如 runoob.com
            buf.append("; Path=\"");
            buf.append(cookie.getPath());
            buf.append("\"");
        }
        if (cookie.getSecure()) {// cookie.getSecure() 该方法设置布尔值，表示 cookie 是否应该只在加密的（即 SSL）连接上发送。
            buf.append("; Secure");
        }

        if (cookie.getVersion() > 0) {//cookie.getVersion() 获取版本号
            buf.append("; Version=\"");
            buf.append(cookie.getVersion());
            buf.append("\"");
        }

        return (buf.toString());
    	
    }
    /**
     * 为敏感字符过滤指定的消息字符串在HTML中。
     * 这避免了包含JavaScript可能造成的攻击请求URL中经常在错误消息中报告的代码。
     * @param message 是要被过滤的字符串
     * return  把message中特殊的字符串替换
     */
    public static String filter(String message){
        if (message == null)
            return (null);
        
        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                result.append("&lt;");
                break;
            case '>':
                result.append("&gt;");
                break;
            case '&':
                result.append("&amp;");
                break;
            case '"':
                result.append("&quot;");
                break;
            default:
                result.append(content[i]);
            }
        }
        return (result.toString());

    }
    
    /**
     * 规范化可能具有相对值("/./")的相对URI路径，“/ . .(等等)。WARNING -
     * 这个方法是仅用于规范化应用程序生成的路径。它不尝试执行恶意输入的安全检查。
     * 
     * 把请求的路径规范化
     */
    public static String normalize(String path) {
    	if(path == null)
    		return null;
    	
    	//为规范化路径创建一个位置
    	String normalized = path;
    	
        if (normalized.equals("/."))
            return "/";
    	
        ////如有需要，可加上“/”
        if (!normalized.startsWith("/"))//没有以/ 开头则加上/
            normalized = "/" + normalized;
        
      //解决规范化路径中出现的“//”
        while (true) {
        	int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                normalized.substring(index + 1);//去掉一个/
        	
        }
      //解决“/./”的问题。在归一化路径中
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                normalized.substring(index + 2);
        }
        //解决“/..”在归一化路径中
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // 试图跳出我们的语境
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                normalized.substring(index + 3);
        }
      //返回我们已经完成的规范化路径
        return (normalized);
    }
    /**
     * 解析来自指定内容类型标头的字符编码。如果内容类型为null，
     * 或者没有显式字符编码，返回null。
     */
    public static String parseCharacterEncoding(String contentType) {
        if (contentType == null)
            return (null);
        int start = contentType.indexOf("charset=");
        if (start < 0)
            return (null);
        String encoding = contentType.substring(start + 8);//encoding:charset=********;******
        int end = encoding.indexOf(';');
        if (end >= 0)
            encoding = encoding.substring(0, end);//encoding示例：charset=utf-8;
        encoding = encoding.trim();
        if ((encoding.length() > 2) && (encoding.startsWith("\""))
                && (encoding.endsWith("\"")))
                encoding = encoding.substring(1, encoding.length() - 1);
            return (encoding.trim());
          
    }
    /**
     * 根据RFC 2109将cookie头解析为cookie数组。
     *name 示例值：   Cookie: userName=name; password=pwd;
     * @param HTTP“Cookie”报头的报头值
     */
    public static Cookie[] parseCookieHeader(String header){
    	
        if ((header == null) || (header.length() < 1))
            return (new Cookie[0]);
        
        ArrayList cookies = new ArrayList();
        while (header.length() > 0) {//semicolon 分号  循环去cookie 字符串 组成cookie
            int semicolon = header.indexOf(';');
            if (semicolon < 0)
                semicolon = header.length();
            if (semicolon == 0)
                break;
            String token = header.substring(0, semicolon);//最后一个是token     用分号分割的每个键值对
            if (semicolon < header.length())
                header = header.substring(semicolon + 1);
            else
                header = "";
            try {
            int equals = token.indexOf('=');
            if (equals > 0) {
                String name = token.substring(0, equals).trim();
                String value = token.substring(equals+1).trim();
                cookies.add(new Cookie(name, value));//加入cookie
            }
            } catch (Throwable e) {
                ;
            }

        }
        return ((Cookie[]) cookies.toArray(new Cookie[cookies.size()]));
    }
    
    //按照指定的编码处理Parameters
    public static void parseParameters(Map map, String data, String encoding)
    		throws UnsupportedEncodingException{
        if ((data != null) && (data.length() > 0)) {
            int len = data.length();
            byte[] bytes = new byte[len];
            data.getBytes(0, len, bytes, 0);
            parseParameters(map, bytes, encoding);
        }
	
    }
    /**
     * 解码并返回指定的url编码字符串。
     * 当字节数组转换为字符串时，系统默认使用字符编码…这可能与其他一些服务器不同。
     * @param str
     * @return
     */
    
    public static String URLDecode(String str) {

        return URLDecode(str, null);

    }
    /**
     * 解码并返回指定的url编码字符串。
     *@param enc 使用的编码;如果为null，则使用默认编码
     */
    public static String URLDecode(String str, String enc) {

        if (str == null)
            return (null);

        int len = str.length();
        byte[] bytes = new byte[len];
        str.getBytes(0, len, bytes, 0);

        return URLDecode(bytes, enc);

    }
    /**
     * 解码并返回指定的url编码字节数组。
     * @param bytes
     * @return
     */
    public static String URLDecode(byte[] bytes) {
        return URLDecode(bytes, null);
    }

    /**
     * 解码并返回指定的url编码字节数组。
     */
    public static String URLDecode(byte[] bytes, String enc) {

        if (bytes == null)
            return (null);
        
        int len = bytes.length;
        int ix = 0;
        int ox = 0;
        while (ix < len) {
        	 byte b = bytes[ix++];  
        	 if (b == '+') {
                 b = (byte)' ';//+ 转 ' '
             } else if (b == '%') {
                 b = (byte) ((convertHexDigit(bytes[ix++]) << 4)
                         + convertHexDigit(bytes[ix++]));
             }
             bytes[ox++] = b;
        }
        if (enc != null) {
            try {
                return new String(bytes, 0, ox, enc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String(bytes, 0, ox);

    }
    
    /**
     * 将字节字符值转换为十六进制数字值。
     * 
     * 字节值'0'-'9' 为 0-9
     * 字节值'a'-'f' 或者 'A'-'F' 为 10-15
     */
    private static byte convertHexDigit( byte b ) {
        if ((b >= '0') && (b <= '9')) return (byte)(b - '0');// (byte) '0' = 48 
        if ((b >= 'a') && (b <= 'f')) return (byte)(b - 'a' + 10);// (byte) 'a' = 97
        if ((b >= 'A') && (b <= 'F')) return (byte)(b - 'A' + 10);// (byte) 'A' = 65
        return 0;
    }
    
    /**
     * 在映射中放入名称值对。
     * 在映射中放入名称和值对。当名称已经存在时，添加值到值数组
     */
    private static void putMapEntry( Map map, String name, String value) {
        String[] newValues = null;
        String[] oldValues = (String[]) map.get(name);
        if (oldValues == null) {
            newValues = new String[1];
            newValues[0] = value;
        }else {
            newValues = new String[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);//复制到新的数组
            newValues[oldValues.length] = value;
        }
        map.put(name, newValues);
    }
    /**
     * 解码经过编码得url获取请求参数
     * @param map
     * @param data
     * @param encoding
     */
    public static void parseParameters(Map map, byte[] data, String encoding)
    		throws UnsupportedEncodingException {
    	if(data != null && data.length > 0){
            int    pos = 0;
            int    ix = 0;
            int    ox = 0;
            String key = null;
            String value = null;
            while(ix < data.length){
            	byte c = data[ix++];
            	switch((char) c){
            	case '&':// 在遇到下一个'&' 之前 date存的就是value
            		value = new String(data, 0, ox, encoding);
            		if(key != null){
            			 putMapEntry(map, key, value);
                         key = null;
            		}
                    ox = 0;
                    break;
                    
            	 case '=':// key=value
                     key = new String(data, 0, ox, encoding);
                     ox = 0;
                     break;
                 case '+'://URL 中+号表示空格      
                     data[ox++] = (byte)' ';
                     break;
                 case '%'://%   一般是遇到上不可显示的字符，会转义成 % + 数值的方式来显示。 unicode编码
                     data[ox++] = (byte)((convertHexDigit(data[ix++]) << 4)
                                     + convertHexDigit(data[ix++]));
                     break;
                 default:
                     data[ox++] = c;         	
            	}
  
            }
            //最后一个值不以'&'结尾。所以现在就保存它。
            if (key != null) {
                value = new String(data, 0, ox, encoding);
                putMapEntry(map, key, value);
            }
            	
            	
            	
    	}
    	
    	
    }
    public static void main(String[] args)  {
    	
    	char a='A';
    	System.out.println((byte)a==65);
    	String str="%3D";
    	byte[] bt=str.getBytes();
    	for (byte b : bt) {
    		System.out.println(convertHexDigit(b)<<4);
		}
    	System.out.println((char)(-85));
    
    }
    
    private  void test1() throws UnsupportedEncodingException{

   	 ParameterMap results=new ParameterMap();
		String s="rsv_spt=1&rsv_iqid=0xf6f4af8000002b6b&issp=1&f=3&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=0&rsv_t=bcc5eVQo3f07JoLo%2FHBEbIwu1Nz%2FNmlCDh%2Fe87yERYXs5ViOjnO%2F%2FToABrixEf532LRr&oq=url%25E4%25B8%25AD%25E7%259A%2584%2526lt%253B%25E8%25BD%25AC%25E4%25B8%25AD%25E6%2596%2587&inputT=2064&rsv_pq=ee4574fa0001821a&rsv_sug3=152&rsv_sug1=155&rsv_sug7=100&prefixsug=url%25E4%25B8%25AD%25E7%259A%2584%2526lt%253B&rsp=0&rsv_sug4=2788&rsv_sug=1";
		parseParameters(results,s.getBytes(),"ISO-8859-1");
		
		for (Object key : results.keySet()) { 
			  System.out.println("Key = " + key+"  value"+Arrays.toString((String[])results.get(key.toString()))); 
			} 
		
	
    }
    

}