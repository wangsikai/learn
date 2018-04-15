package com.lanking.cloud.component.db.masterslave;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RUNTIME)
@Documented
public @interface MasterSlaveDataSource {
	/**
	 * 数据源类型,可选为[M|MM|MS|S],默认为M,目前只支持M|MS
	 * 
	 * <pre>
	 * M:此接口W|RW（写读有依赖关系并且不允许有数据延迟）
	 * MS:此接口R|W|RW（读写没有依赖关系或者允许数据延迟）
	 * </pre>
	 * 
	 * @return
	 */
	String type() default "MS";
}
