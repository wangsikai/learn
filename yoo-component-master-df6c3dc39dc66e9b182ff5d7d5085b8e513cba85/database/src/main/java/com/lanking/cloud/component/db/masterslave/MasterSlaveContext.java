package com.lanking.cloud.component.db.masterslave;

public class MasterSlaveContext {

	private static final ThreadLocal<String> MASTERSLAVE = new ThreadLocal<String>();

	public static void set(String ms) {
		MASTERSLAVE.set(ms);
	}

	public static String get() {
		return MASTERSLAVE.get();
	}

	public static boolean isMaster() {
		return "M".equals(get());
	}

	public static boolean isMasterSlave() {
		return "MS".equals(get());
	}

}