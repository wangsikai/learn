package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 教材版本
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "textbook_category")
public class TextbookCategory implements Serializable {

	private static final long serialVersionUID = 390385494090057199L;

	/**
	 * 代码
	 */
	@Id
	private Integer code;

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
	 * 悠数学中小学阶段是否支持
	 * 
	 * <pre>
	 * Status.ENABLED:表示悠数学中可用状态,即小学阶段支持此版本
	 * </pre>
	 */
	@Column(name = "primary_status", precision = 3, columnDefinition = "tinyint default 1")
	private Status primaryStatus = Status.DISABLED;

	/**
	 * 悠数学中中学阶段是否支持
	 * 
	 * <pre>
	 * Status.ENABLED:表示悠数学中可用状态,即中学阶段支持此版本
	 * </pre>
	 */
	@Column(name = "middle_status", precision = 3, columnDefinition = "tinyint default 1")
	private Status middleStatus = Status.DISABLED;

	/**
	 * 悠数学中高中阶段是否支持
	 * 
	 * <pre>
	 * Status.ENABLED:表示悠数学中可用状态,即高中阶段支持此版本
	 * </pre>
	 */
	@Column(name = "high_status", precision = 3, columnDefinition = "tinyint default 1")
	private Status highStatus = Status.DISABLED;

	/**
	 * 悠数学中的状态
	 * 
	 * <pre>
	 * Status.ENABLED:表示悠数学中可用状态,即支持此版本,优先级高于阶段支持的状态
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

	public Status getPrimaryStatus() {
		return primaryStatus;
	}

	public void setPrimaryStatus(Status primaryStatus) {
		this.primaryStatus = primaryStatus;
	}

	public Status getMiddleStatus() {
		return middleStatus;
	}

	public void setMiddleStatus(Status middleStatus) {
		this.middleStatus = middleStatus;
	}

	public Status getHighStatus() {
		return highStatus;
	}

	public void setHighStatus(Status highStatus) {
		this.highStatus = highStatus;
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

	/**
	 * 阶段是否支持
	 * 
	 * @param codePhase
	 *            阶段代码
	 * @return true|false
	 */
	public boolean support(Integer codePhase) {
		if (codePhase == null) {
			return false;
		} else {
			if (codePhase.intValue() == 1 && this.primaryStatus == Status.ENABLED) {
				return true;
			} else if (codePhase.intValue() == 2 && this.middleStatus == Status.ENABLED) {
				return true;
			} else if (codePhase.intValue() == 3 && this.highStatus == Status.ENABLED) {
				return true;
			} else {
				return false;
			}
		}
	}

}
