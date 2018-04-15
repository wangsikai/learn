package com.lanking.cloud.component.db.support.hibernate;

@SuppressWarnings("rawtypes")
public interface ResultExtractor<T> {

	boolean accept(Class targetClass);

	Object extract(Class targetClass, T result);
}
