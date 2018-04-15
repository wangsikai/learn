package com.lanking.uxb.service.index.api;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 索引mapping定义
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月4日
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RUNTIME)
@Documented
public @interface IndexType {

	com.lanking.cloud.domain.type.IndexType type();

	// TODO _source 属性配置
	boolean source() default true;

}
