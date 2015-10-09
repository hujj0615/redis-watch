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

	public boolean ping() {
		try {
			writeCommand(Constants.COMMAND_PING);
			byte[] data = readSimpleString(false);
			return "PONG".equals(new String(data));
		} catch (IOException e) {
			throw new RedisClientException("error execute ping command", e);
		}
	}

    public byte[] get(byte[] key) {
        try {
            writeCommand(Constants.COMMAND_GET, key);
            byte[] data = readBulkString(false);
            return data;
        } catch (IOException e) {
            throw new RedisClientException("error execute get command", e);
        }
    }

    public boolean set(byte[] key, byte[] value) {
        try {
            writeCommand(Constants.COMMAND_SET, key, value);
            
            byte[] data = readSimpleString(false);
            return "OK".equals(new String(data));
        } catch (IOException e) {
            throw new RedisClientException("error execute set command", e);
        }
    }

    public boolean set(byte[] key, byte[] value, int expireSeconds) {
        try {
            writeCommand(Constants.COMMAND_SET, key, value, numberToDecimalBytes(expireSeconds));
            
            //TODO a Null Bulk Reply is returned if the SET operation was not performed
            byte[] data = readSimpleString(false);
            return "OK".equals(new String(data));
        } catch (IOException e) {
            throw new RedisClientException("error execute set command", e);
        }
    }

}
