package com.lanking.cloud.component.db.sql;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface SqlRenderer {

	String render(Class entityClass, String key);

	String render(Class entityClass, String key, Map<String, Object> params);

	String render(String tplName, String key);

	String render(String tplName, String key, Map<String, Object> params);
}
