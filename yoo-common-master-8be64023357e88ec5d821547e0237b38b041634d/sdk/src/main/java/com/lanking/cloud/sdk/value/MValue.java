package com.lanking.cloud.sdk.value;

import com.lanking.cloud.ex.AbstractException;

/**
 * 手机端返回数据结构
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月8日
 */
public class MValue extends Value {

	private static final long serialVersionUID = 3285855094904359004L;

	private String ret_token;

	public String getRet_token() {
		return ret_token;
	}

	public void setRet_token(String ret_token) {
		this.ret_token = ret_token;
	}

	public MValue(String token, Value value) {
		super();
		this.ret_token = token;
		setRet(value.getRet());
		setRet_code(value.getRet_code());
		setRet_msg(value.getRet_msg());
	}

	public MValue(String token) {
		super();
		this.ret_token = token;
	}

	public MValue(int ret_code, String ret_msg, Object ret, String token) {
		super(ret_code, ret_msg, ret);
		this.ret_token = token;
	}

	public MValue(int ret_code, String ret_msg, String token) {
		super(ret_code, ret_msg);
		this.ret_token = token;
	}

	public MValue(Object ret, String token) {
		super(ret);
		this.ret_token = token;
	}

	public MValue(AbstractException e, Object ret, String token) {
		super(e, ret);
		this.ret_token = token;
	}

	public MValue(AbstractException e, String token) {
		super(e);
		this.ret_token = token;
	}

}
