/**
 * 活动参与用户vo
 */
package com.lanking.uxb.zycon.activity.value;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
public class VZycActivityUser {

	private ImperialExaminationGrade grade;

	/**
	 * 报填手机号码
	 */
	private String applyName;

	private VUser user;
	/**
	 * 报填手机号码
	 */
	private String applyMobile;

	/**
	 * 班级
	 */
	private List<VHomeworkClazz> clazz;

	private Date createAt;

	public ImperialExaminationGrade getGrade() {
		return grade;
	}

	public void setGrade(ImperialExaminationGrade grade) {
		this.grade = grade;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyMobile() {
		return applyMobile;
	}

	public void setApplyMobile(String applyMobile) {
		this.applyMobile = applyMobile;
	}

	public List<VHomeworkClazz> getClazz() {
		return clazz;
	}

	public void setClazz(List<VHomeworkClazz> clazz) {
		this.clazz = clazz;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
