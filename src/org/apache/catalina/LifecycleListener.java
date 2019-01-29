package org.apache.catalina;

public interface LifecycleListener {

	/**
	 *  监听事件的发生 做出响应的操作
	 * @param event
	 */
    public void lifecycleEvent(LifecycleEvent event);
}
