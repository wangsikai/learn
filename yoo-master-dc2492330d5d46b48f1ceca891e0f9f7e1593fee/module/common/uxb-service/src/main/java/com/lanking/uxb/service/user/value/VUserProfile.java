package com.lanking.uxb.service.user.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月16日
 *
 */
public class VUserProfile extends VUser {

	private static final long serialVersionUID = 895916281882459174L;

	private VAccount account;
	private Object info;

	@JsonIgnore
	private VTeacherProfile tp;
	@JsonIgnore
	private VStudentProfile sp;
	@JsonIgnore
	private VParentProfile pp;

	public VAccount getAccount() {
		return account;
	}

	public void setAccount(VAccount account) {
		this.account = account;
	}

	public Object getInfo() {
		if (info == null) {
			if (getType() == UserType.TEACHER) {
				setInfo(getTp());
			} else if (getType() == UserType.STUDENT) {
				setInfo(getSp());
			} else if (getType() == UserType.PARENT) {
				setInfo(getPp());
			}
		}
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	public VTeacherProfile getTp() {
		return tp;
	}

	public void setTp(VTeacherProfile tp) {
		this.tp = tp;
	}

	public VStudentProfile getSp() {
		return sp;
	}

	public void setSp(VStudentProfile sp) {
		this.sp = sp;
	}

	public VParentProfile getPp() {
		return pp;
	}

	public void setPp(VParentProfile pp) {
		this.pp = pp;
	}

}
