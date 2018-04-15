package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 教材
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "textbook")
public class Textbook implements Serializable {

	private static final long serialVersionUID = -1142638430785167008L;

	/**
	 * 教材代码
	 */
	@Id
	private Integer code;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 版本代码
	 */
	@Column(name = "category_code")
	private Integer categoryCode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 图标(file space使用cover)
	 */
	@Column(name = "icon")
	private Long icon;

	/**
	 * 序号
	 */
	@Column(name = "sequence", columnDefinition = "bigint default 0")
	private Integer sequence;

	/**
	 * 悠数学中的状态
	 * 
	 * <pre>
	 * Status.ENABLED:表示悠数学中可用状态,即支持此教材
	 * </pre>
	 */
	@Column(name = "yoomath_status", precision = 3, columnDefinition = "tinyint default 1")
	private Status yoomathStatus = Status.DISABLED;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIcon() {
		return icon;
	}

	public void setIcon(Long icon) {
		this.icon = icon;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Status getYoomathStatus() {
		return yoomathStatus;
	}

	public void setYoomathStatus(Status yoomathStatus) {
		this.yoomathStatus = yoomathStatus;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
