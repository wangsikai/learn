package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 教材章节
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "section")
public class Section implements Serializable {

	private static final long serialVersionUID = 9083584603962329099L;

	/**
	 * 章节代码
	 */
	@Id
	private Long code;

	/**
	 * 父级代码
	 */
	@Column(name = "pcode")
	private Long pcode;

	/**
	 * 对应教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 层级
	 */
	@Column(name = "level", precision = 3)
	private Integer level;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

	/**
	 * 序号
	 */
	@Column(name = "sequence", columnDefinition = "bigint default 0")
	private Integer sequence;

	/**
	 * 判断是否为本章综合与测试.
	 * 
	 * @param section
	 *            章节
	 * @return
	 */
	public boolean isComprehensiveSection() {
		return getName().equals("本章综合与测试");
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
