package com.lanking.uxb.service.adminSecurity.support;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 管理系统api角色过滤
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface ConsoleRolesAllowed {

	/**
	 * 角色code集合，表示拥有这些角色用户可以访问此接口
	 * 具体code 查看 con_role 表
	 *
	 * e.g. roleCodes = { "CSADMIN", "PG" }
	 *
	 * @return
	 */
	String[] roleCodes() default {};

	/**
	 * API方法可以被任何人（包括未登录）用户使用，注意此属性标注后将不会判断roles. <br>
	 *
	 * @return
	 */
	boolean anyone() default false;

	/**
	 * 是否只有管理员才可以访问(优于角色判断)
	 *
	 * @return
	 */
	boolean systemAdmin() default false;
}
