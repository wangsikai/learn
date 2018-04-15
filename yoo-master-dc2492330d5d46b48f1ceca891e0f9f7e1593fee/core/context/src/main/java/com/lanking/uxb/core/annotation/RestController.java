package com.lanking.uxb.core.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Controller;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
@Controller
public @interface RestController {

	String value() default "";

	boolean token() default false;

	boolean clearToken() default false;
}
