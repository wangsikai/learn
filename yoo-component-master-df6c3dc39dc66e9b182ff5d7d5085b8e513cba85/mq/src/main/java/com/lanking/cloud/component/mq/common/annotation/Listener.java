package com.lanking.cloud.component.mq.common.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface Listener {

	// 队列
	String queue();

	// 队列-持久化
	boolean queueDurable() default true;

	// 队列-排他,谁声明谁销毁,适用于单个订阅者的场景
	boolean queueExclusive() default false;

	// 自动删除,如果此队列没有订阅者,则此队列会被删除,适用于临时队列
	boolean queueAutoDelete() default false;

	// 是否需要保证时序性（目前只对ExchangeTypes.DIRECT有效）；如果设置为false,exclusive参数失效
	boolean series() default true;

	String routingKey() default "";

	// channel相关
	int prefetchCount() default 1;

	boolean autoAck() default false;

	boolean requeue() default true;

	boolean exclusive() default true;

	// 是否消费（如果为false则相当于只定义了队列,无消费者）
	boolean consume() default true;

}
