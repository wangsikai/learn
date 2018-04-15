package com.lanking.uxb.service.cache.api;

public enum SerializerType {
	DEFAULT("default"), SPRING("spring"), KRYO("kryo"), STRING("string");

	private String value;

	SerializerType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
