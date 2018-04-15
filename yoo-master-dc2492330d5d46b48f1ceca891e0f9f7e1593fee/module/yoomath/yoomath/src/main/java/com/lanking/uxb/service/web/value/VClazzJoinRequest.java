package com.lanking.uxb.service.web.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequestStatus;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 学生申请加入班级VO
 * 
 * @author wangsenhao
 *
 */
public class VClazzJoinRequest implements Serializable {

	private static final long serialVersionUID = 2256632523727872444L;

	private long id;
	private VUserProfile student;
	// 申请时候填的名称
	private String realName;
	private VHomeworkClazz clazz;
	// 申请时间
	private Date requestAt;
	// 请求处理状态
	private ClazzJoinRequestStatus requestStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public VUserProfile getStudent() {
		return student;
	}

	public void setStudent(VUserProfile student) {
		this.student = student;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public VHomeworkClazz getClazz() {
		return clazz;
	}

	public void setClazz(VHomeworkClazz clazz) {
		this.clazz = clazz;
	}

	public Date getRequestAt() {
		return requestAt;
	}

	public void setRequestAt(Date requestAt) {
		this.requestAt = requestAt;
	}

	public ClazzJoinRequestStatus getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(ClazzJoinRequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

}
