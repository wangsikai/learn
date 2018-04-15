package com.lanking.uxb.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * api的一些控制和记录
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月9日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface ApiAllowed {

	/**
	 * 访问频率<br>
	 * -1:no rate limit<br>
	 * 0:use default(表示同一时间只能访问一次接口)<br>
	 * >0:频率控制现在还没实现
	 * 
	 * @return access rate second
	 */
	int accessRate() default -1;
}
