package com.lanking.uxb.service.index.api;

public enum MappingType {
	/**
	 * String
	 */
	TEXT, KEYWORD,
	/**
	 * Numeric datatypes
	 */
	LONG, INTEGER, SHORT, BYTE, DOUBLE, FLOAT,
	/**
	 * Date datatype
	 */
	DATE,
	/**
	 * Boolean datatype
	 */
	BOOLEAN,
	/**
	 * IP datatype
	 */
	IP;

	public String type() {
		return this.name().toLowerCase();
	}
}
