package com.littledudu.redis.watch.util;

/**
 * @author hujinjun
 * @date 2015-9-28 
 */
public interface TimerTaskListener {
	void beforeRun(int number);
	void afterRun(int number);
}
