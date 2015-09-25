package com.littledudu.redis.watch.client;

/**
 * @author hujinjun
 * @date 2015-9-25 
 */
public class RedisClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1352060031498409281L;

	public RedisClientException() {
		super();
	}

	public RedisClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisClientException(String message) {
		super(message);
	}

	public RedisClientException(Throwable cause) {
		super(cause);
	}

}
