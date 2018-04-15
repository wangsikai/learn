package com.lanking.uxb.service.account.value;

import com.lanking.uxb.service.user.value.VUser;

/**
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月11日
 */
public class VMStuSession extends VMSession {

	private static final long serialVersionUID = 2867093381839797187L;

	// 是否需要完善信息(true|false,true时则需要设置教材)
	private boolean needSetTextbook = true;

	public VMStuSession() {
		super();
	}

	public VMStuSession(VUser user) {
		super(user);
	}

	public boolean isNeedSetTextbook() {
		return needSetTextbook;
	}

	public void setNeedSetTextbook(boolean needSetTextbook) {
		this.needSetTextbook = needSetTextbook;
	}

}
