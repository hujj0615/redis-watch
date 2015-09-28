package com.littledudu.redis.watch;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.littledudu.redis.watch.util.ParamKeyConvertor;

public class InfoTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		Map<String, String> map = new HashMap<String, String>();
		String[] lines = TEST_INFO_RESPONSE.split("\r\n");
		for(String line : lines) {
			if(line.startsWith("#"))
				continue;
			int pos = line.indexOf(':');
			if(pos > -1) {
				String key = line.substring(0, pos);
				String value = line.substring(pos + 1);
				map.put(key, value);
			}
		}
		Info info = ParamKeyConvertor.setupByParamKey(map, Info.class);
		assertNotNull(info);
		assertNotNull(info.getRedisVersion());
		assertTrue(info.getProcessId() > 0);
		assertTrue(info.getPort() > 0);
		System.out.println(info);
	}

	private static final String TEST_INFO_RESPONSE = 		"# Server\r\n" + 
			"redis_version:2.8.5\r\n" + 
			"process_id:8455\r\n" + 
			"tcp_port:6379\r\n" + 
			"uptime_in_seconds:999\r\n" + 
			"uptime_in_days:2\r\n" + 
			"# Clients\r\n" + 
			"connected_clients:6\r\n" + 
			"client_longest_output_list:0\r\n" + 
			"client_biggest_input_buf:0\r\n" + 
			"blocked_clients:0\r\n" + 
			"# Memory\r\n" + 
			"used_memory:167379432\r\n" + 
			"used_memory_human:159.63M\r\n" + 
			"used_memory_rss:174428160\r\n" + 
			"used_memory_peak:169314336\r\n" + 
			"used_memory_peak_human:161.47M\r\n" + 
			"used_memory_lua:33792\r\n" + 
			"mem_fragmentation_ratio:1.04\r\n" + 
			"mem_allocator:jemalloc-3.2.0\r\n" + 
			"# Stats\r\n" + 
			"total_connections_received:33\r\n" + 
			"total_commands_processed:48\r\n" + 
			"instantaneous_ops_per_sec:0\r\n" + 
			"rejected_connections:0\r\n" + 
			"sync_full:0\r\n" + 
			"sync_partial_ok:0\r\n" + 
			"sync_partial_err:0\r\n" + 
			"expired_keys:2\r\n" + 
			"evicted_keys:0\r\n" + 
			"keyspace_hits:7\r\n" + 
			"keyspace_misses:5\r\n" + 
			"pubsub_channels:0\r\n" + 
			"pubsub_patterns:0\r\n" + 
			"latest_fork_usec:0\r\n";
}
