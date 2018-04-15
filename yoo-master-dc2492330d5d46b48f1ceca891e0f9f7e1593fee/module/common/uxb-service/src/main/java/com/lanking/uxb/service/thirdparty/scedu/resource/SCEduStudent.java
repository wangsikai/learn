package com.lanking.uxb.service.thirdparty.scedu.resource;

import java.io.Serializable;
import java.util.List;

/**
 * 学生.
 * 
 * @author wlche
 *
 */
public class SCEduStudent implements Serializable {
	private static final long serialVersionUID = 372781851978463848L;

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
