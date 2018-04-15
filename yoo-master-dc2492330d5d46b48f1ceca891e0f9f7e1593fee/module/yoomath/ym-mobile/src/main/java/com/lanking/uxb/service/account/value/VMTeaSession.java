package com.lanking.uxb.service.account.value;

import com.lanking.uxb.service.user.value.VUser;

/**
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月11日
 */
public class VMTeaSession extends VMSession {

	private static final long serialVersionUID = 5030159961769606478L;

	// 教师端是否需要创建班级
	private boolean needCreateClass = false;

	// 教师端是否需要完善资料
	private boolean needPerfectData = false;

	public VMTeaSession() {
		super();
	}

	public VMTeaSession(VUser user) {
		super(user);
	}

	public boolean isNeedCreateClass() {
		return needCreateClass;
	}

	public void setNeedCreateClass(boolean needCreateClass) {
		this.needCreateClass = needCreateClass;
	}

	public boolean isNeedPerfectData() {
		return needPerfectData;
	}

	public void setNeedPerfectData(boolean needPerfectData) {
		this.needPerfectData = needPerfectData;
	}

}
