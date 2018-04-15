package com.lanking.cloud.sdk.util;

public final class UUID {

	public static String uuid() {
		return java.util.UUID.randomUUID().toString().replaceAll("-", "");
	}

	private UUID() {
	}

}
