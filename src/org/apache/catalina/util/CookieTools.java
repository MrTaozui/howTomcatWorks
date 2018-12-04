package org.apache.catalina.util;

import java.text.FieldPosition;
import java.util.Date;

import javax.servlet.http.Cookie;

/**
 * 生成cookie头等
 * @author
 *
 */
public class CookieTools {
	
/**
 * 基于Cookie版本返回头名称设置cookie
 */
    public static String getCookieHeaderName(Cookie cookie) {
        int version = cookie.getVersion();

        if (version == 1) {
            return "Set-Cookie2";
        } else {
            return "Set-Cookie";
        }
    }
    
    
    public static String getCookieHeaderValue(Cookie cookie) {
        StringBuffer buf = new StringBuffer();
        getCookieHeaderValue( cookie, buf );
        return buf.toString();
    }
    
    /** 返回用于设置此Cookie的头值
     */
    public static void getCookieHeaderValue(Cookie cookie, StringBuffer buf) {
        int version = cookie.getVersion();

        // this part is the same for all cookies

        String name = cookie.getName();     // Avoid NPE on malformed cookies
        if (name == null)
            name = "";
        String value = cookie.getValue();
        if (value == null)
            value = "";

        buf.append(name);
        buf.append("=");
        maybeQuote(version, buf, value);

        // add version 1 specific information
        if (version == 1) {
            // Version=1 ... required
            buf.append (";Version=1");

            // Comment=comment
            if (cookie.getComment() != null) {
                buf.append (";Comment=");
                maybeQuote (version, buf, cookie.getComment());
            }
        }

        // add domain information, if present

        if (cookie.getDomain() != null) {
            buf.append(";Domain=");
            maybeQuote (version, buf, cookie.getDomain());
        }

        // Max-Age=secs/Discard ... or use old "Expires" format
        if (cookie.getMaxAge() >= 0) {
            if (version == 0) {
                buf.append (";Expires=");
                if (cookie.getMaxAge() == 0)//cookie 最大存活时间
                    DateTool.oldCookieFormat.format(new Date(10000), buf,
                                                    new FieldPosition(0));
                else
                    DateTool.oldCookieFormat.format
                        (new Date( System.currentTimeMillis() +
                                   cookie.getMaxAge() *1000L), buf,
                         new FieldPosition(0));
            } else {
                buf.append (";Max-Age=");
                buf.append (cookie.getMaxAge());
            }
        } else if (version == 1)
          buf.append (";Discard");

        // Path=path
        if (cookie.getPath() != null) {
            buf.append (";Path=");
            maybeQuote (version, buf, cookie.getPath());
        }

        // Secure
        if (cookie.getSecure()) {
          buf.append (";Secure");
        }
    }

    static void maybeQuote (int version, StringBuffer buf,
                                    String value)
    {
        if (version == 0 || isToken (value))
            buf.append (value);
        else {
            buf.append ('"');
            buf.append (value);
            buf.append ('"');
        }
    }

        //
    // 从RFC 2068，令牌的特殊案例的特点
    //
    private static final String tspecials = "()<>@,;:\\\"/[]?={} \t";

    /*
     * 返回true IFF字符串作为HTTP/1.1“令牌” 规范。
     */
    private static boolean isToken (String value) {
        int len = value.length ();

        for (int i = 0; i < len; i++) {
            char c = value.charAt (i);

            if (c < 0x20 || c >= 0x7f || tspecials.indexOf (c) != -1)
              return false;
        }
        return true;
    }



}
