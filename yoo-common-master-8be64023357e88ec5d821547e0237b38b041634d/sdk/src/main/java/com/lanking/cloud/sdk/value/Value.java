package com.lanking.cloud.sdk.value;

import java.io.Serializable;

import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.code.StatusCode;

/**
 * web端返回数据结构
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月8日
 */
public class Value implements Serializable {

	private static final long serialVersionUID = 6600815387487084135L;

	private int ret_code = StatusCode.SUCCEED;

	private String ret_msg = "OK";

	private Object ret;

	public Value() {
		super();
	}

	public Value(AbstractException e) {
		super();
		this.ret_code = e.getCode();
		this.ret_msg = e.getMessage();
	}

	public Value(AbstractException e, Object ret) {
		super();
		this.ret_code = e.getCode();
		this.ret_msg = e.getMessage();
		this.ret = ret;
	}

	public Value(Object ret) {
		super();
		this.ret = ret;
	}

	public Value(int ret_code, String ret_msg) {
		super();
		this.ret_code = ret_code;
		this.ret_msg = ret_msg;
	}

	public Value(int ret_code, String ret_msg, Object ret) {
		super();
		this.ret_code = ret_code;
		this.ret_msg = ret_msg;
		this.ret = ret;
	}

	public int getRet_code() {
		return ret_code;
	}

	public void setRet_code(int ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public Object getRet() {
		return ret;
	}

	public void setRet(Object ret) {
		this.ret = ret;
	}

}
