package com.lanking.cloud.component.db.sql.httl;

import httl.Engine;
import httl.Template;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.ex.core.ServerException;

@SuppressWarnings("rawtypes")
public class SqlRendererImpl implements SqlRenderer {
	private Engine engine;

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	@Override
	public String render(Class entityClass, String key) {
		return render(entityClass.getSimpleName(), key);
	}

	@Override
	public String render(Class entityClass, String key, Map<String, Object> params) {
		return render(entityClass.getSimpleName(), key, params);
	}

	@Override
	public String render(String tplName, String key) {
		return render(tplName, key, Collections.<String, Object> emptyMap());
	}

	@Override
	public String render(String tplName, String key, Map<String, Object> params) {
		try {
			Template sqlTpl = engine.getTemplate(getTplPath(tplName)).getMacros().get(key);
			if (sqlTpl == null) {
				throw new ServerException("Macro [" + key + "] not found in " + getTplPath(tplName));
			}
			return ((String) sqlTpl.evaluate(params)).trim();
		} catch (ParseException e) {
			throw new ServerException(e);
		} catch (IOException e) {
			throw new ServerException(e);
		}
	}

	private String getTplPath(String tplName) {
		return tplName + ".sql";
	}
}
