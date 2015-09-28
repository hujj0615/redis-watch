package com.littledudu.redis.watch.client;

import java.nio.charset.Charset;

/**
 * @author hujinjun
 * @date 2015-9-25 
 */
public final class Constants {
	
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 6379;
	
	public static final Charset CHAR_SET = Charset.forName("UTF-8");
	
	public static final byte[] COMMAND_INFO = "info".getBytes(CHAR_SET);
	public static final byte[] COMMAND_PING = "ping".getBytes(CHAR_SET);
}
