package com.lanking.cloud.sdk.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月22日
 *
 */
public final class Order implements Serializable {

	private static final long serialVersionUID = 6285805100711077135L;
	public static final Direction DEFAULT_DIRECTION = Direction.DESC;
	private final String field;
	private final Direction direction;

	public Order(String field, Direction direction) {
		this.field = field;
		this.direction = direction;
	}

	public String getField() {
		return field;
	}

	@JSONField(serialize = false)
	public Direction getDirection() {
		return direction;
	}

	public boolean isAsc() {
		return direction.isAsc();
	}

	public static Order order(String field, Direction direction) {
		return new Order(field, direction);
	}

	public static Order order(String field, boolean asc) {
		return order(field, asc ? Direction.ASC : Direction.DESC);
	}

	public static Order order(String field) {
		return order(field, DEFAULT_DIRECTION);
	}

	public static Order asc(String field) {
		return order(field, true);
	}

	public static Order desc(String field) {
		return order(field, false);
	}

	public static List<Order> orders(boolean asc, String... fields) {
		if (fields == null) {
			return Collections.emptyList();
		}
		List<Order> orders = Lists.newArrayListWithCapacity(fields.length);
		for (String field : fields) {
			orders.add(Order.order(field, asc));
		}
		return orders;
	}

	public static List<Order> orders(String... fields) {
		return orders(DEFAULT_DIRECTION.isAsc(), fields);
	}

	@Override
	public int hashCode() {
		return field.hashCode() << 1 | direction.ordinal();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Order)) {
			return false;
		}
		Order other = (Order) obj;
		return direction == other.direction && field.equals(other.field);
	}

	@Override
	public String toString() {
		return field + " " + direction.getValue();
	}

	public static enum Direction {
		ASC("asc"), DESC("desc");

		private String value;

		private Direction(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public boolean isAsc() {
			return this == ASC;
		}
	}
}
