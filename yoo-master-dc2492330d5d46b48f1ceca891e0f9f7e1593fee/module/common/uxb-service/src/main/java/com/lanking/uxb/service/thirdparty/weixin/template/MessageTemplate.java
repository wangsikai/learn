package com.lanking.uxb.service.thirdparty.weixin.template;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息模板.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月4日
 */
public class MessageTemplate {

	/**
	 * 标题.
	 */
	private String first;

	/**
	 * REMARK 消息内容.
	 */
	private String remark;

	/**
	 * 获得消息体.
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public Object getObject() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Field[] fields = this.getClass().getDeclaredFields();
		Map<String, Object> map = new HashMap<String, Object>(fields.length);
		if (null != this.getFirst()) {
			Map<String, Object> dmap = new HashMap<String, Object>(1);
			dmap.put("value", this.getFirst());
			map.put("first", dmap);
		}
		if (null != this.getRemark()) {
			Map<String, Object> dmap = new HashMap<String, Object>(1);
			dmap.put("value", this.getRemark());
			map.put("remark", dmap);
		}
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			String value = String.valueOf(f.get(this));
			if (null != value) {
				Map<String, Object> dmap = new HashMap<String, Object>(1);
				dmap.put("value", value);
				map.put(f.getName(), dmap);
			}
		}
		return map;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
