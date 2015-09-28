package com.littledudu.redis.watch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.littledudu.redis.watch.client.RedisClient;

public class RedisClientTest {
	
	private RedisClient client = new RedisClient("localhost", 6379);
	
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
	
	@Test
	public void testPing() {
		assertTrue(client.ping());
		assertTrue(client.ping());
		assertTrue(client.ping());
		assertTrue(client.ping());
		System.out.println("after ping");
	}
}
