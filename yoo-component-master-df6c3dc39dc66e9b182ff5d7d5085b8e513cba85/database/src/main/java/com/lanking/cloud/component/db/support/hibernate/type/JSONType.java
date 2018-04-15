package com.lanking.cloud.component.db.support.hibernate.type;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

import javax.persistence.Column;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.SerializationHelper;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONType implements UserType, DynamicParameterizedType, Serializable {

	private static final long serialVersionUID = 277245730151400451L;

	private Logger logger = LoggerFactory.getLogger(JSONType.class);

	public static final String TYPE = "com.lanking.cloud.component.db.support.hibernate.type.JSONType";

	public static final String CLASS_NAME = "class";
	private int sqlType = Types.VARCHAR;
	@SuppressWarnings("rawtypes")
	private Class clazz = Object.class;
	private Type type = clazz;

	@Override
	public int[] sqlTypes() {
		return new int[] { sqlType };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return clazz;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object object = rs.getObject(names[0]);
		if (rs.wasNull()) {
			return null;
		} else {
			return JSON.parseObject((String) object, type);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, sqlType);
		} else {
			st.setObject(index, JSON.toJSONString(value), sqlType);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		if (value instanceof JSONObject) {
			return ((JSONObject) value).clone();
		} else if (value instanceof Cloneable) {
			return ObjectUtils.clone(value);
		} else if (value instanceof Serializable) {
			return SerializationHelper.clone((Serializable) value);
		} else {
			return value;
		}
	}

	@Override
	public boolean isMutable() {
		return true;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public void setParameterValues(Properties parameters) {
		try {
			clazz = ReflectHelper.classForName(parameters.getProperty(DynamicParameterizedType.ENTITY));
			Field field = ReflectionUtils.findField(clazz, parameters.getProperty(DynamicParameterizedType.PROPERTY));
			type = field.getGenericType();
			parseSqlType(field.getAnnotations());
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		final ParameterType reader = (ParameterType) parameters.get(DynamicParameterizedType.PARAMETER_TYPE);
		if (reader != null) {
			clazz = reader.getReturnedClass();
			parseSqlType(reader.getAnnotationsMethod());
		} else {
			try {
				clazz = ReflectHelper.classForName((String) parameters.get(CLASS_NAME));
				if (type instanceof Class) {
					Class tClass = (Class) type;
					if (tClass.isInterface() || Modifier.isAbstract(tClass.getModifiers())) {
						type = clazz;
					}
				}
			} catch (ClassNotFoundException exception) {
				throw new HibernateException("class not found", exception);
			}
		}
	}

	private void parseSqlType(Annotation[] anns) {
		for (Annotation an : anns) {
			if (an instanceof Column) {
				int length = ((Column) an).length();
				if (length > 4000) {
					sqlType = Types.CLOB;
				}
				break;
			}
		}
	}
}
