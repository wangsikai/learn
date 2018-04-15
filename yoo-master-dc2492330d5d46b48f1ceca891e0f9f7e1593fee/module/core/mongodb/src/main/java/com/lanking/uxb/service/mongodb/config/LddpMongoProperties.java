package com.lanking.uxb.service.mongodb.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

@ConfigurationProperties(prefix = "lddp.data.mongodb")
public class LddpMongoProperties {

	private Logger logger = LoggerFactory.getLogger(LddpMongoProperties.class);

	private String host;
	private int port = 27017;
	private String database;
	private String username;
	private char[] password;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public char[] getPassword() {
		return this.password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public MongoClient createMongoClient(MongoClientOptions options) {
		try {
			if (options == null) {
				Builder builder = MongoClientOptions.builder();
				builder.readPreference(ReadPreference.nearest());
				options = builder.build();
			}
			List<MongoCredential> credentials = Lists.newArrayList();
			List<ServerAddress> seeds = Lists.newArrayList();
			MongoCredential credential = MongoCredential.createCredential(this.username, this.database, this.password);
			String[] hosts = host.split(",");
			for (String h : hosts) {
				seeds.add(new ServerAddress(h, this.port));
				credentials.add(credential);
			}
			return new MongoClient(seeds, credentials, options);
		} catch (Exception e) {
			logger.error("create mongo client error:", e);
			return null;
		}
	}
}
