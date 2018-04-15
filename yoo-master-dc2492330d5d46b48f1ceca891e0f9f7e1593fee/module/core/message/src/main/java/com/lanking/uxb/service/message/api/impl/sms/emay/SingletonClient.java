package com.lanking.uxb.service.message.api.impl.sms.emay;

import com.lanking.cloud.springboot.environment.Env;

public class SingletonClient {
	private static Client client = null;

	private SingletonClient() {
	}

	public synchronized static Client getClient(String softwareSerialNo, String key) {
		if (client == null) {
			try {
				client = new Client(softwareSerialNo, key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	public synchronized static Client getClient() {
		if (client == null) {
			try {
				client = new Client(Env.getString("sms.emay.softwareSerialNo"), Env.getString("sms.emay.key"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}

}
