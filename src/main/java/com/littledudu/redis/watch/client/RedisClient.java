package com.littledudu.redis.watch.client;

import java.io.IOException;

/**
 * @author hujinjun
 * @date 2015-9-25 
 */
public class RedisClient extends Connection implements Command {

	public RedisClient() {
		super();
	}

	public RedisClient(String host, int port) {
		super(host, port);
	}

	public String info() {
		try {
			writeCommand(Constants.COMMAND_INFO);
			byte[] data = readBulkString(false);
			return new String(data);
		} catch (IOException e) {
			throw new RedisClientException("error execute info command", e);
		}
	}

	public String info(String section) {
		try {
			writeCommand(Constants.COMMAND_INFO, section.getBytes(Constants.CHAR_SET));
			byte[] data = readBulkString(false);
			return new String(data);
		} catch (IOException e) {
			throw new RedisClientException("error execute info command", e);
		}
	}

}
