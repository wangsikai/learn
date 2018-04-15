package com.lanking.uxb.service.session.api.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.lanking.cloud.sdk.bean.Attrable;
import com.lanking.cloud.sdk.util.ArrayUtils;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月19日
 *
 */
public class AttrSession implements Attrable, Serializable, Cloneable {

	private static final long serialVersionUID = 7118988252822405961L;

	private JSONObject attr;

	public AttrSession(JSONObject attr) {
		this.attr = attr;
	}

	public AttrSession(Map<String, Object> map) {
		this.attr = new JSONObject(map);
	}

	public AttrSession() {
		attr = new JSONObject();
	}

	public JSONObject getAttr() {
		return attr;
	}

	public void setAttr(JSONObject attr) {
		this.attr = attr;
	}

	public void updateAttr(JSONObject attr) {
		if (attr != null) {
			if (this.attr == null) {
				this.attr = attr;
			} else {
				for (String key : attr.keySet()) {
					this.attr.put(key, attr.get(key));
				}
			}
		}
	}

	@Override
	public boolean hasAttr(String key) {
		return attr.containsKey(key);
	}

	@Override
	public String getAttr(String key) {
		return attr.getString(key);
	}

	@Override
	public String getAttr(String key, String defaultValue) {
		String v = getAttr(key);
		if (v == null) {
			v = defaultValue;
		}
		return v;
	}

	@Override
	public boolean getBoolAttr(String key) {
		String value = getAttr(key);
		return StringUtils.isEmpty(value) ? false : TypeUtils.castToBoolean(value);
	}

	@Override
	public byte getByteAttr(String key) {
		String value = getAttr(key);
		return StringUtils.isEmpty(value) ? 0 : TypeUtils.castToByte(value);
	}

	@Override
	public short getShortAttr(String key) {
		String value = getAttr(key);
		return StringUtils.isEmpty(value) ? 0 : TypeUtils.castToShort(value);
	}

	@Override
	public int getIntAttr(String key) {
		String value = getAttr(key);
		return StringUtils.isEmpty(value) ? 0 : TypeUtils.castToInt(value);
	}

	@Override
	public long getLongAttr(String key) {
		String value = getAttr(key);
		return StringUtils.isEmpty(value) ? 0L : TypeUtils.castToLong(value);
	}

	@Override
	public float getFloatAttr(String key) {
		String value = getAttr(key);
		return StringUtils.isEmpty(value) ? 0F : TypeUtils.castToFloat(value);
	}

	@Override
	public double getDoubleAttr(String key) {
		String value = getAttr(key);
		return StringUtils.isEmpty(value) ? 0D : TypeUtils.castToDouble(value);
	}

	@Override
	public <T> T getObject(String key, Class<T> clazz) {
		return attr.getObject(key, clazz);
	}

	@Override
	public Collection<String> keys() {
		return attr.keySet();
	}

	@Override
	public Map<String, String> getAttrs(String... keys) {
		return getAttrs(ArrayUtils.asList(keys));
	}

	@Override
	public Map<String, String> getAttrs(Collection<String> keys) {
		if (keys == null) {
			keys = keys();
		}
		Map<String, String> map = new HashMap<String, String>(keys.size());
		for (String key : keys) {
			String value = getAttr(key);
			if (value != null) {
				map.put(key, value);
			}
		}
		return Collections.unmodifiableMap(map);
	}

	@Override
	public void setAttr(String key, Object value) {
		attr.put(key, value);
	}

	@Override
	public void setAttrs(Map<String, ?> map) {
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			setAttr(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void removeAttr(String key) {
		attr.remove(key);
	}

	@Override
	public void removeAttrs(String... keys) {
		removeAttrs(ArrayUtils.asList(keys));
	}

	@Override
	public void removeAttrs(Collection<String> keys) {
		for (String key : keys) {
			attr.remove(key);
		}
	}

	@Override
	public void clearAttrs() {
		removeAttrs(keys());
	}

	@Override
	public AttrSession clone() {
		AttrSession obj = null;
		try {
			obj = (AttrSession) super.clone();
			JSONObject json = new JSONObject(getAttr());
			obj.setAttr(json);
		} catch (CloneNotSupportedException ignored) {
		}
		return obj;
	}
}
