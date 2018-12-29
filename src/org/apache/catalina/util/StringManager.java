package org.apache.catalina.util;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 用于加载错误的信息，配置在每个包下面
 * 加载properties  文件  然后 赋值给 private 的bundle   然后调用getString 方法 得到信息。
 * @author taojiajun
 *
 */
public class StringManager {

	 private ResourceBundle bundle;
	 
	 private StringManager(String packageName) {
	        String bundleName = packageName + ".LocalStrings";
	        this.bundle = ResourceBundle.getBundle(bundleName);
	 }
	 
	 /**
	  * 从底层资源包中获取字符串。
	  * @param key
	  * @return
	  */
	 public String getString(String key) {
	        if (key == null) {
	            String msg = "key is null";

	            throw new NullPointerException(msg);
	        }

	        String str = null;

	        try {
	            str = bundle.getString(key);
	        } catch (MissingResourceException mre) {
	            str = "Cannot find message associated with key '" + key + "'";
	        }

	        return str;
	    }
	 
	 /**
	  * 从底层资源包中获取字符串，并使用给定的参数集格式化。
	  */
	 public String getString(String key, Object[] args){
		 String iString = null;
		 String value = this.getString(key);
		 
		 try{
		 //确保参数不为null，这样1.2 VM前的参数就不会出现问题  
		//String[] strs = {"大","家",null} 	 转为 String[] strs = {"大","家","null"}
		 Object nonNullArgs[] = args;
		 for (int i=0; i<args.length; i++) {
			if(args[i] == null){
				if (nonNullArgs==args) nonNullArgs=(Object[])args.clone();
                nonNullArgs[i] = "null";
			}
		}
		 
		 iString = MessageFormat.format(value, nonNullArgs);
		 }catch (IllegalArgumentException iae) {
			 StringBuffer buf = new StringBuffer();
			 buf.append(value);
			 for (int i = 0; i < args.length; i++) {
	                buf.append(" arg[" + i + "]=" + args[i]);
	            }
	            iString = buf.toString();			 
		 }
		 return iString;
	 }
	 
	    public String getString(String key, Object arg) {
	        Object[] args = new Object[] {arg};
	        return getString(key, args);
	    }

	 
	    public String getString(String key, Object arg1, Object arg2) {
	        Object[] args = new Object[] {arg1, arg2};
	        return getString(key, args);
	    }

	    public String getString(String key, Object arg1, Object arg2,
                Object arg3) {
	    	Object[] args = new Object[] {arg1, arg2, arg3};
	    	return getString(key, args);
	    }

	    public String getString(String key, Object arg1, Object arg2,
                Object arg3, Object arg4) {
	    	Object[] args = new Object[] {arg1, arg2, arg3, arg4};
	    	return getString(key, args);
	    }
	    
	    // --------------------------------------------------------------
	    // STATIC SUPPORT METHODS   用到的时候，加载在其中
	    // --------------------------------------------------------------
	    private static Hashtable managers = new Hashtable();
	    
	    public synchronized static StringManager getManager(String packageName) {
	        StringManager mgr = (StringManager)managers.get(packageName);
	        if (mgr == null) {
	            mgr = new StringManager(packageName);
	            managers.put(packageName, mgr);
	        }
	        return mgr;
	    }
	    
	    
	 public static void main(String[] args) {
		String[] strs={"大","家","null"};
		System.out.println(MessageFormat.format("{0},{1},{2}", strs));
	}
}
