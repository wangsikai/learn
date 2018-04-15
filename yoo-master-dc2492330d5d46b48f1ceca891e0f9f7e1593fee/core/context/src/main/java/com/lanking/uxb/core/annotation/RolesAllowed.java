package com.lanking.uxb.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Method's annotation. <br>
 * 标注在有RequestMapping的API接口，当标注在类上时，作用域为该类所有API，
 * 当方法及类上均有标注时，以方法上为准。当properties文件中safe.ignores有URI配置时，
 * 相同URI的API方法标注权限将被忽略，safe.ignores优先级高于此标注。
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2014年12月5日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface RolesAllowed {

	/**
	 * API方法可以通过用户TYPE集合. <br>
	 * e.g. @RolesAllowed(userTypes = {"TEACHER", "STUDENT"}).
	 * 
	 * @return
	 */
	String[] userTypes() default {};

	/**
	 * API方法可以被任何人（包括未登录）用户使用，注意此属性标注后将不会判断roles. <br>
	 * e.g. @RolesAllowed(anyone = true).
	 * 
	 * @return
	 */
	boolean anyone() default false;
}
