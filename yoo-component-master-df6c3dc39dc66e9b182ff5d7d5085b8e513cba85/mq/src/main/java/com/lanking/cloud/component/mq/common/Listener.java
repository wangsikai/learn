package com.lanking.cloud.component.mq.common;

import java.lang.reflect.Method;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Listener {

	private String queue;
	private boolean series;

	private int prefetchCount;
	private boolean autoAck;
	private boolean requeue;
	boolean exclusive = true;
	boolean consume = true;

	@JSONField(serialize = false)
	private Object handler;
	@JSONField(serialize = false)
	private Method method;

}
