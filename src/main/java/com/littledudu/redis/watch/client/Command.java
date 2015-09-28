package com.littledudu.redis.watch.client;

/**
 * @author hujinjun
 * @date 2015-9-25 
 */
public interface Command {
	String info();
	String info(String section);
	boolean ping();
}
