package com.lanking.cloud.sdk.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import com.alibaba.fastjson.annotation.JSONField;
import com.lanking.cloud.sdk.util.ArrayUtils;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 */
public class PageRequest implements Pageable, Serializable {

	private static final long serialVersionUID = 4668590265538872363L;
	private final int offset;
	private final int size;
	private final Collection<Order> orders;

	public PageRequest(int offset, int size, Collection<Order> orders) {
		if (offset < 0) {
			throw new IllegalArgumentException("Offset must not be less than zero!");
		}
		this.offset = offset;
		this.size = size;
		this.orders = orders;
	}

	public PageRequest(int offset, int size, Order... orders) {
		this(offset, size, ArrayUtils.asList(orders));
	}

	public PageRequest(int offset, int size) {
		this(offset, size, Collections.<Order> emptyList());
	}

	public PageRequest(int offset) {
		this(offset, SIZE_DEFAULT);
	}

	public PageRequest() {
		this(0);
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	@JSONField(serialize = false)
	public int getOffset() {
		return offset;
	}

	@Override
	public int getIndex() {
		return toIndex(offset, size);
	}

	@Override
	public Collection<Order> getOrders() {
		return orders;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + offset;
		result = 31 * result + size;
		result = 31 * result + orders.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Pageable)) {
			return false;
		}
		Pageable other = (Pageable) obj;
		return offset == other.getOffset() && size == other.getSize() && orders.equals(other.getOrders());
	}

	@Override
	public String toString() {
		return "offset:" + offset + ", size:" + size + (orders.isEmpty() ? "" : ", orders:" + orders.toString());
	}

	@Override
	public Pageable next() {
		return new PageRequest(offset + size, size, orders);
	}

	@Override
	public Pageable previous() {
		return new PageRequest(offset - size, size, orders);
	}

	public static int toOffset(int index, int size) {
		return (index - 1) * size;
	}

	public static int toIndex(int offset, int size) {
		return offset / size + 1;
	}

	@Override
	public Pageable current() {
		return this;
	}
}
