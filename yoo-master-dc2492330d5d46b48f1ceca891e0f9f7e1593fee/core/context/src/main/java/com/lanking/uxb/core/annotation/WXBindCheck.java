package com.lanking.uxb.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 微信绑定校验.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface WXBindCheck {

	/**
	 * API方法可以通过用户TYPE. <br>
	 * e.g. @RolesAllowed(userTypes = "TEACHER").
	 * 
	 * @return
	 */
	String userType() default "TEACHER";

	/**
	 * 产品来源.
	 * 
	 * @return
	 */
	String product() default "YOOMATH";
}
