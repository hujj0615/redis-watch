package com.littledudu.redis.watch.client;

/**
 * @author hujinjun
 * @date 2015-9-25 
 */
public interface Command {
	String info();
	String info(String section);
	boolean ping();
	byte[] get(byte[] key);
	boolean set(byte[] key, byte[] value);
	boolean set(byte[] key, byte[] value, int expireSeconds);
}
