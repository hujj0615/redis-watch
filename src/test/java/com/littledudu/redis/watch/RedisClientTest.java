package com.littledudu.redis.watch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.littledudu.redis.watch.client.RedisClient;

public class RedisClientTest {
	
	private RedisClient client = new RedisClient("10.160.128.52", 6389);
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testInfo() {
		try {
			String info = client.info();
			assertNotNull(info);
			System.out.println(info);
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testInfoString() {
		try {
			String info = client.info("stats");
			assertNotNull(info);
			System.out.println(info);
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}

}