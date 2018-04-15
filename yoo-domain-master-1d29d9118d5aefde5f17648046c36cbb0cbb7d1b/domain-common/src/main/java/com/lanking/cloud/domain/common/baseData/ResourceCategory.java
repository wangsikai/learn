package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 资源类别,一级类别代码对应ResourceCategoryCode
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 * @see ResourceCategoryCode
 */
@Entity
@Table(name = "resource_category")
public class ResourceCategory implements Serializable {

	private static final long serialVersionUID = -6889329317657139767L;

	/**
	 * 类别代码
	 */
	@Id
	private int code;

	/**
	 * 父级代码
	 */
	@Column(name = "pcode")
	private int pcode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 序号
	 */
	@Column(name = "sequence", columnDefinition = "bigint default 0")
	private Integer sequence;

	/**
	 * 状态
	 */
	@Column(precision = 3, columnDefinition = "tinyint default 0")
	private Status status = Status.ENABLED;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getPcode() {
		return pcode;
	}

	public void setPcode(int pcode) {
		this.pcode = pcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
