package com.lanking.uxb.service.session.form;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class VerifyCode implements Serializable {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -3891528027507799590L;

	/**
	 * ID
	 */
	private Long id;

	private Integer[][] location;
	
	private Date deadline;
	
	private List<String> characters;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer[][] getLocation() {
		return location;
	}

	public void setLocation(Integer[][] location) {
		this.location = location;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public List<String> getCharacters() {
		return characters;
	}

	public void setCharacters(List<String> characters) {
		this.characters = characters;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
