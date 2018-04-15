package com.lanking.uxb.service.index.value;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * index doc 抽象超类
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月4日
 */
@SuppressWarnings("serial")
public abstract class AbstraceIndexDoc implements Serializable {

	public String documentValue() {
		return JSON.toJSON(this).toString();
	}
}
