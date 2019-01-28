package org.apache.catalina;

public interface LifecycleListener {

	/**
	 * 确认指定事件的发生。
	 * @param event
	 */
    public void lifecycleEvent(LifecycleEvent event);
}
