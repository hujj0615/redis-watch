package com.littledudu.redis.watch;

import java.io.Serializable;

import com.littledudu.redis.watch.util.ParamKey;

/**
 * @author hujinjun
 * @date 2015-9-25 
 */
public class Info implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 154170582699222266L;

	@ParamKey("redis_version")
	private String redisVersion;
	
	@ParamKey("process_id")
	private int processId;
	
	@ParamKey("tcp_port")
	private int port;
	
	@ParamKey("uptime_in_seconds")
	private long uptimeSeconds;

	@ParamKey("connected_clients")
	private int connectedClients;

	@ParamKey("used_memory")
	private long usedMemory;

	@ParamKey("total_connections_received")
	private long totalConnectionsReceived;
	
	@ParamKey("total_commands_processed")
	private long totalCommandsProcessed;
	
	@ParamKey("instantaneous_ops_per_sec")
	private int instantaneousOpsPerSec;
	
	@ParamKey("expired_keys")
	private long expiredKeys;
	
	@ParamKey("evicted_keys")
	private long evictedKeys;
	
	@ParamKey("keyspace_hits")
	private long keyspaceHits;
	
	@ParamKey("keyspace_misses")
	private long keyspaceMisses;

	public String getRedisVersion() {
		return redisVersion;
	}

	public void setRedisVersion(String redisVersion) {
		this.redisVersion = redisVersion;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getUptimeSeconds() {
		return uptimeSeconds;
	}

	public void setUptimeSeconds(long uptimeSeconds) {
		this.uptimeSeconds = uptimeSeconds;
	}

	public int getConnectedClients() {
		return connectedClients;
	}

	public void setConnectedClients(int connectedClients) {
		this.connectedClients = connectedClients;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public long getTotalConnectionsReceived() {
		return totalConnectionsReceived;
	}

	public void setTotalConnectionsReceived(long totalConnectionsReceived) {
		this.totalConnectionsReceived = totalConnectionsReceived;
	}

	public long getTotalCommandsProcessed() {
		return totalCommandsProcessed;
	}

	public void setTotalCommandsProcessed(long totalCommandsProcessed) {
		this.totalCommandsProcessed = totalCommandsProcessed;
	}

	public int getInstantaneousOpsPerSec() {
		return instantaneousOpsPerSec;
	}

	public void setInstantaneousOpsPerSec(int instantaneousOpsPerSec) {
		this.instantaneousOpsPerSec = instantaneousOpsPerSec;
	}

	public long getExpiredKeys() {
		return expiredKeys;
	}

	public void setExpiredKeys(long expiredKeys) {
		this.expiredKeys = expiredKeys;
	}

	public long getEvictedKeys() {
		return evictedKeys;
	}

	public void setEvictedKeys(long evictedKeys) {
		this.evictedKeys = evictedKeys;
	}

	public long getKeyspaceHits() {
		return keyspaceHits;
	}

	public void setKeyspaceHits(long keyspaceHits) {
		this.keyspaceHits = keyspaceHits;
	}

	public long getKeyspaceMisses() {
		return keyspaceMisses;
	}

	public void setKeyspaceMisses(long keyspaceMisses) {
		this.keyspaceMisses = keyspaceMisses;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Info [redisVersion=");
		builder.append(redisVersion);
		builder.append(", processId=");
		builder.append(processId);
		builder.append(", port=");
		builder.append(port);
		builder.append(", uptimeSeconds=");
		builder.append(uptimeSeconds);
		builder.append(", connectedClients=");
		builder.append(connectedClients);
		builder.append(", usedMemory=");
		builder.append(usedMemory);
		builder.append(", totalConnectionsReceived=");
		builder.append(totalConnectionsReceived);
		builder.append(", totalCommandsProcessed=");
		builder.append(totalCommandsProcessed);
		builder.append(", instantaneousOpsPerSec=");
		builder.append(instantaneousOpsPerSec);
		builder.append(", expiredKeys=");
		builder.append(expiredKeys);
		builder.append(", evictedKeys=");
		builder.append(evictedKeys);
		builder.append(", keyspaceHits=");
		builder.append(keyspaceHits);
		builder.append(", keyspaceMisses=");
		builder.append(keyspaceMisses);
		builder.append("]");
		return builder.toString();
	}
}
