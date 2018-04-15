package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 地区
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "district")
public class District implements Serializable {

	private static final long serialVersionUID = -9097935761892311082L;

	/**
	 * 代码
	 */
	@Id
	private long code;

	/**
	 * 父级代码
	 */
	@Column(name = "pcode")
	private long pcode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 层级
	 */
	@Column(name = "level")
	private int level;

	/**
	 * 首字母
	 */
	@Column(name = "acronym", length = 40)
	private String acronym;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getPcode() {
		return pcode;
	}

	public void setPcode(long pcode) {
		this.pcode = pcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
