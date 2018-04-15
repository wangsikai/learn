package com.lanking.uxb.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 会员权限控制
 * 
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年9月27日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface MemberAllowed {

	/**
	 * 默认为NONE表示此接口权限由内部实现 ,如果是具体的会员类型（写最小的会员类型），则表示调用此接口的用户必须符合此会员类型
	 * 
	 * @since 2.5.0
	 * @return 会员类型
	 */
	String memberType() default "NONE";
}
