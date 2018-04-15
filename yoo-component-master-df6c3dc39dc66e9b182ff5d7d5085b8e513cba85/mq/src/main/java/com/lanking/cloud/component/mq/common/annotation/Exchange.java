package com.lanking.cloud.component.mq.common.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.amqp.core.ExchangeTypes;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface Exchange {

	String name();

	String type() default ExchangeTypes.DIRECT;

	boolean durable() default true;

	boolean autoDelete() default false;

	boolean internal() default false;

	boolean delayed() default false;
}
