package com.lanking.uxb.service.thirdparty.scedu.resource;

import java.io.Serializable;

/**
 * 班级信息.
 * 
 * @author wlche
 *
 */
public class SCEduClass implements Serializable {
	private static final long serialVersionUID = 1494167182901880886L;
	private String classId;
	private String classname;
	private String gradename;
	private Integer section;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getGradename() {
		return gradename;
	}

	public void setGradename(String gradename) {
		this.gradename = gradename;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}
}
