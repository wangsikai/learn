package com.lanking.cloud.sdk.data;

import static com.lanking.cloud.sdk.data.PageRequest.toOffset;

import java.util.Collection;
import java.util.Collections;

import com.lanking.cloud.sdk.util.ArrayUtils;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 */
public final class P {

	public static Pageable offset(int offset, int size, Order... orders) {
		return offset(offset, size, ArrayUtils.asList(orders));
	}

	public static Pageable offset(int offset, int size, Collection<Order> orders) {
		return new PageRequest(offset, size, orders);
	}

	public static Pageable offset(int offset, int size, boolean asc, String... fields) {
		return offset(offset, size, Order.orders(asc, fields));
	}

	public static Pageable offset(int offset, int size, String... fields) {
		return offset(offset, size, Order.orders(fields));
	}

	public static Pageable offset(int offset, int size) {
		return offset(offset, size, Collections.<Order> emptyList());
	}

	public static Pageable offset(int offset, Order... orders) {
		return offset(offset, Pageable.SIZE_DEFAULT, orders);
	}

	public static Pageable offset(int offset) {
		return offset(offset, offset);
	}

	public static Pageable index(int index, int size, Order... orders) {
		return offset(toOffset(index, size), size, orders);
	}

	public static Pageable index(int index, int size, Collection<Order> orders) {
		return offset(toOffset(index, size), size, orders);
	}

	public static Pageable index(int index, int size, boolean asc, String... fields) {
		return offset(toOffset(index, size), size, asc, fields);
	}

	public static Pageable index(int index, int size, String... fields) {
		return offset(toOffset(index, size), size, fields);
	}

	public static Pageable index(int index, int size) {
		return offset(toOffset(index, size), size);
	}

	public static Pageable index(int index, Order... orders) {
		return offset(toOffset(index, Pageable.SIZE_DEFAULT), orders);
	}

	public static Pageable index(int index) {
		return offset(toOffset(index, Pageable.SIZE_DEFAULT), Pageable.SIZE_DEFAULT);
	}

	public static Pageable first(int size, Order... orders) {
		return offset(Pageable.OFFSET_FIRST, size, orders);
	}

	public static Pageable first(int size, Collection<Order> orders) {
		return offset(Pageable.OFFSET_FIRST, size, orders);
	}

	public static Pageable first(int size, boolean asc, String... fields) {
		return offset(Pageable.OFFSET_FIRST, size, asc, fields);
	}

	public static Pageable first(int size, String... fields) {
		return offset(Pageable.OFFSET_FIRST, size, fields);
	}

	public static Pageable first(int size) {
		return offset(Pageable.OFFSET_FIRST, size);
	}

	public static Pageable first() {
		return offset(Pageable.OFFSET_FIRST, Pageable.SIZE_DEFAULT);
	}

	public static Pageable all() {
		return offset(Pageable.OFFSET_FIRST, Pageable.SIZE_ALL);
	}

	public static Pageable none() {
		return offset(Pageable.OFFSET_FIRST, Pageable.SIZE_NONE);
	}

	private P() {
	}
}
