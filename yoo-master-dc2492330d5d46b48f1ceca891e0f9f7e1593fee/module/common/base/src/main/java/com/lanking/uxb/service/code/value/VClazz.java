package com.lanking.uxb.service.code.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.util.StringUtils;

public class VClazz implements Serializable {

	private static final long serialVersionUID = -2951470705199118678L;

	private long id;
	private String name;
	private String description;
	private String code;

	private long schoolId;
	private int enterYear;
	private int phaseCode;
	private int system;
	private int gradeCode;

	private String schoolName;
	private String gradeName;
	private String fullName;
	private String phaseName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public int getEnterYear() {
		return enterYear;
	}

	public void setEnterYear(int enterYear) {
		this.enterYear = enterYear;
	}

	public int getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(int phaseCode) {
		this.phaseCode = phaseCode;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public int getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(int gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getFullName() {
		String nameTemp = getName();
		if (StringUtils.isBlank(fullName) && nameTemp != null) {
			if (nameTemp.startsWith("0")) {
				nameTemp = nameTemp.substring(1, nameTemp.length());
			}
			setFullName(getGradeName() + "(" + nameTemp + ")" + "班");
		}
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.id == ((VClazz) obj).getId();
	}
}
