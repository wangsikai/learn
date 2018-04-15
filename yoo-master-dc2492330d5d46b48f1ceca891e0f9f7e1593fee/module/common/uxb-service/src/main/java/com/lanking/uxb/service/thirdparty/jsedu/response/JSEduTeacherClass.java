package com.lanking.uxb.service.thirdparty.jsedu.response;

import java.io.Serializable;

/**
 * 教师班级信息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月30日
 */
public class JSEduTeacherClass implements Serializable {
	private static final long serialVersionUID = -581771895787577249L;

	private String techType;

	private String className;

	private String schName;

	private String schId;

	private String classId;

	public String getTechType() {
		return techType;
	}

	public void setTechType(String techType) {
		this.techType = techType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSchName() {
		return schName;
	}

	public void setSchName(String schName) {
		this.schName = schName;
	}

	public String getSchId() {
		return schId;
	}

	public void setSchId(String schId) {
		this.schId = schId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
}
