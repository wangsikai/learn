package com.lanking.uxb.service.thirdparty.eduyun.response;

import java.io.Serializable;

/**
 * 班级信息.
 * 
 * @author wlche
 *
 */
public class YunClass implements Serializable {
	private static final long serialVersionUID = 5628868735217155895L;

	/**
	 * 班级ID.
	 */
	private String classid;

	/**
	 * 学科ID.
	 */
	private String subjectid;

	/**
	 * 学科名称.
	 */
	private String subjectname;

	/**
	 * 版本ID.
	 */
	private String teachmaterialid;

	/**
	 * 版本名称.
	 */
	private String teachmaterialname;

	/**
	 * 教材ID.
	 */
	private String volumeid;

	/**
	 * 教材名称.
	 */
	private String volumename;

	/**
	 * 班级名称.
	 */
	private String classname;

	/**
	 * 班级身份(1:班主任 2：班长 3：管理员 4: 教师 5：成员 6：教研员，7：教务员，8：信息员).
	 */
	private String[] classidentity;

	/**
	 * .
	 */
	private String type;

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}

	public String getSubjectname() {
		return subjectname;
	}

	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}

	public String getTeachmaterialid() {
		return teachmaterialid;
	}

	public void setTeachmaterialid(String teachmaterialid) {
		this.teachmaterialid = teachmaterialid;
	}

	public String getTeachmaterialname() {
		return teachmaterialname;
	}

	public void setTeachmaterialname(String teachmaterialname) {
		this.teachmaterialname = teachmaterialname;
	}

	public String getVolumeid() {
		return volumeid;
	}

	public void setVolumeid(String volumeid) {
		this.volumeid = volumeid;
	}

	public String getVolumename() {
		return volumename;
	}

	public void setVolumename(String volumename) {
		this.volumename = volumename;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String[] getClassidentity() {
		return classidentity;
	}

	public void setClassidentity(String[] classidentity) {
		this.classidentity = classidentity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
