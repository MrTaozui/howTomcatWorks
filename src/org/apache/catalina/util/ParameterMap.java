package org.apache.catalina.util;

import java.util.HashMap;
import java.util.Map;
/**
 * ParameterMap 继承自 HashMap 
 * @author taojiajun
 *
 */
public class ParameterMap extends HashMap{
	//使用默认初始容量和构造一个新的空映射加载因子。
	public ParameterMap() {
		super();
	}
	//用指定的初始容量和构造一个新的空映射默认加载因子。
	public ParameterMap(int initialCapacity) {
        super(initialCapacity);
    }
	//用指定的初始容量和构造一个新的空映射加载因子。
	 public ParameterMap(int initialCapacity, float loadFactor) {
	        super(initialCapacity, loadFactor);
	    }
	 //使用与给定映射相同的映射构造一个新的映射。
	 public ParameterMap(Map map) {
	        super(map);
	    }

	  // ------------------------------------------------------------- 属性
	 
	 private boolean locked = false;//此参数映射的当前锁定状态。  被锁定之后不能操作参数
	 
	 public boolean isLocked() {

	        return (this.locked);

	    }
	 public void setLocked(boolean locked) {

	        this.locked = locked;

	    }
	 //此包下面的StringManager
	 private static final StringManager sm =
		        StringManager.getManager("org.apache.catalina.util");
	 
	    // --------------------------------------------------------- 公共方法
	 
	 /**
	  * 删除所有的参数映射
	  */
	   public void clear() {

	        if (locked)
	            throw new IllegalStateException
	                (sm.getString("parameterMap.locked"));
	        super.clear();

	    }
	   // 放入参数
	    public Object put(Object key, Object value) {

	        if (locked)
	            throw new IllegalStateException
	                (sm.getString("parameterMap.locked"));
	        return (super.put(key, value));

	    }
	    // 放入参数
	    public void putAll(Map map) {

	        if (locked)
	            throw new IllegalStateException
	                (sm.getString("parameterMap.locked"));
	        super.putAll(map);

	    }
	    //移除属性
	    public Object remove(Object key) {

	        if (locked)
	            throw new IllegalStateException
	                (sm.getString("parameterMap.locked"));
	        return (super.remove(key));

	    }

	    
	    
}
