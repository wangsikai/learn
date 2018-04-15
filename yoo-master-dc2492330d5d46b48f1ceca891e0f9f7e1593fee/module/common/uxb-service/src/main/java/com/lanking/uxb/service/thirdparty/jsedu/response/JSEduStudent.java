package com.lanking.uxb.service.thirdparty.jsedu.response;

import java.io.Serializable;

/**
 * 学生信息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月30日
 */
public class JSEduStudent implements Serializable {
	private static final long serialVersionUID = -1931880590981939596L;

	private String name;

	private String schName;

	private String sex;

	private String nation;

	private String nativePlace;

	private String gradeName;

	private String className;

	private String serialnum;

	private String schId;

	private String classId;

	/**
	 * 结果返回码.
	 */
	private String resultCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchName() {
		return schName;
	}

	public void setSchName(String schName) {
		this.schName = schName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSerialnum() {
		return serialnum;
	}

	public void setSerialnum(String serialnum) {
		this.serialnum = serialnum;
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

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
}
