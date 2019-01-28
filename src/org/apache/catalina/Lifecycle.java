package org.apache.catalina;

/**
 * 生命周期
 * @author taojiajun
 *
 */
public interface Lifecycle {

	/**
	 * 组件启动 生命周期事件
	 */
	public static final String START_EVENT = "start";
	/**
	 * 生命周期事件  组件启动之前
	 */
	 public static final String BEFORE_START_EVENT = "before_start";
	 
	 /**
	  * 生命周期事件 组件启动之后
	  */
	 public static final String AFTER_START_EVENT = "after_start";
	 
	 /**
	  * 生命周期事件 组件停止
	  */
	 public static final String STOP_EVENT = "stop";

	 /**
	  * 生命周期事件 组件停止之前
	  */
	 public static final String BEFORE_STOP_EVENT = "before_stop";
	 
	 /**
	  * 生命周期事件 组件停止之后
	  */
	 public static final String AFTER_STOP_EVENT = "after_stop";
	 
	 /**
	  * 添加一个 生命周期事件监听  给这个组件
	  */
	 public void addLifecycleListener(LifecycleListener lister);
	 
	 /**
	  * 获取这个生命周期相关的监听  如果这个生命周期没有注册，0长度的数组返回
	  * @return
	  */
	 public LifecycleListener[] findLifecycleListeners();
	 
	 /**
	  * 从组件中移除一个生命周期事件
	  * @param listener
	  */
	 public void removeLifecycleListener(LifecycleListener listener);
	 
	 /**
	  * 准备开始使用此组件的公共方法。 这个方法必须在任意公共方法使用之前。 它也需要发送一个START_EVENT的生命周期事件
	  * 给任意的已注册的监听
	  * @throws LifecycleException
	  */
	 public void start() throws LifecycleException;
	 
	 /**
	  * 优雅的结束 组件的在活动的公共方法。这个方法必须在给定此组件的实例最后调用。
	  * 它也需要发送一个STOP_EVENT的生命周期事件 给任意的已注册的监听
	  * @throws LifecycleException
	  */
	 public void stop() throws LifecycleException;
}
