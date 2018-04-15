package com.lanking.uxb.service.thirdparty.scedu.resource;

import java.io.Serializable;
import java.util.List;

/**
 * 教师信息.
 * 
 * @author wlche
 *
 */
public class SCEduTeacher implements Serializable {
	private static final long serialVersionUID = 3464294160051799670L;

	/**
	 * 学校ID.
	 */
	private String schoolId;

	/**
	 * 学校名称.
	 */
	private String schoolOrgName;

	/**
	 * 班级列表.
	 */
	private List<SCEduClass> clazzes;

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolOrgName() {
		return schoolOrgName;
	}

	public void setSchoolOrgName(String schoolOrgName) {
		this.schoolOrgName = schoolOrgName;
	}

	public List<SCEduClass> getClazzes() {
		return clazzes;
	}

	public void setClazzes(List<SCEduClass> clazzes) {
		this.clazzes = clazzes;
	}
}
