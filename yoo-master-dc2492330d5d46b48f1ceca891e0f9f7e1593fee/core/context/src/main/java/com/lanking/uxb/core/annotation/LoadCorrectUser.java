package com.lanking.uxb.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 加载对应的小悠快批用户.
 * <p>
 * 在方法上添加该注解，这样在方法内部即可使用SecurityContext类获取到correctUser的ID，以及通过session获取correctUserResponse对象.
 * </p>
 * 
 * @since 小悠快批，2019-3-14
 * @author wanlong.che
 *
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface LoadCorrectUser {

	/**
	 * 是否加载，默认为true.
	 * 
	 * @return
	 */
	boolean value() default true;
}
