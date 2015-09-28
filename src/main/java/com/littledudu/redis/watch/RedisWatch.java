package com.littledudu.redis.watch;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.littledudu.redis.watch.client.Constants;
import com.littledudu.redis.watch.client.RedisClient;
import com.littledudu.redis.watch.util.ParamKeyConvertor;
import com.littledudu.redis.watch.util.TimerTaskListener;

/**
 * @author hujinjun
 * @date 2015-9-28 
 */
public class RedisWatch extends TimerTask {

	private String host;
	private int port;
	private RedisClient client;
	private boolean firstTime = true;
	private int loopNumber;
	
	private TimerTaskListener listener;
	
	public RedisWatch(String host, int port) {
		this(host, port, null);
	}
	public RedisWatch(String host, int port, TimerTaskListener listener) {
		super();
		this.host = host;
		this.port = port;
		loopNumber = 0;
		this.listener = listener;
		client = new RedisClient(host, port);
	}
	
	@Override
	public void run() {
		loopNumber++;
		if(listener != null) {
			listener.beforeRun(loopNumber);
		}
		
		watchSimple();
		
		if(listener != null) {
			listener.afterRun(loopNumber);
		}
	}
	
	private void watchSimple() {
		if(firstTime) {
			if(client.ping() ) {
				System.out.println(String.format("connected to redis: %s:%d", host, port));
				System.out.format("%10s%12s%12s%10s%10s%12s%12s\r\n", "used_mem", "total_cmd", "ops_per_sec", "expd_keys", "evit_keys", "hit", "miss");
			} else {
				System.out.println("ping failed, exit...");
				System.exit(-1);
			}
		}
		String infoMsg = client.info();
		Info info = stringToInfo(infoMsg);
		System.out.format("%10s%12d%5d%10d%10d%12d%12d\r\n", humanReadableByteCount(info.getUsedMemory()), info.getTotalCommandsProcessed(),
				info.getInstantaneousOpsPerSec(),
				info.getExpiredKeys(),
				info.getEvictedKeys(),
				info.getKeyspaceHits(),
				info.getKeyspaceMisses()
				);
		
		firstTime = false;
	}
	
	
	public static String humanReadableByteCount(long bytes) {
	    int unit = 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = "KMGTPE".charAt(exp-1) + "i";
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	private Info stringToInfo(String info) {
		Map<String, String> map = new HashMap<String, String>();
		String[] lines = info.split("\r\n");
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
		return ParamKeyConvertor.setupByParamKey(map, Info.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		long period = Long.valueOf(args[0]);
		final int loopCount = Integer.valueOf(args[1]);
		String host = Constants.DEFAULT_HOST;
		int port = Constants.DEFAULT_PORT;
		if(args.length >= 4) {
			host = args[2];
			port = Integer.valueOf(args[3]);
		}
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new RedisWatch(host, port, new TimerTaskListener() {

			public void beforeRun(int number) {
			}

			public void afterRun(int number) {
				if(number >= loopCount) {
					timer.cancel();
				}
			}}), 0, period);
	}

}
