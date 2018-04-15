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
@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Documented
public @interface IndexMapping {

	/**
	 * @return 类型
	 */
	String index() default "";

	/**
	 * 是否包含在_all字段里面
	 * 
	 * @return true|false
	 */
	boolean includeInAll() default false;

	/**
	 * 是否忽略此字段
	 * 
	 * @return true|false
	 */
	boolean ignore() default false;

	/**
	 * 字段类型
	 * 
	 * @return {@link MappingType}
	 */
	MappingType type();

	/**
	 * 索引分词ik_max_word
	 * 
	 * @return {@link String}
	 */
	String analyzer() default "";

	/**
	 * 搜索分词ik_max_word
	 * 
	 * @return {@link String}
	 */
	String searchAnalyzer() default "";

	// TODO 是否存储
	boolean store() default true;

}
