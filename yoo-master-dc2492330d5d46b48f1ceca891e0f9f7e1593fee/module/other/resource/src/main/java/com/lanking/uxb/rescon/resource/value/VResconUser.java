package com.lanking.uxb.rescon.resource.value;

/**
 * User -> VResconUser
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public class VResconUser {
	private Long id;
	private String name;
	private String schoolName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
}
