package com.lanking.cloud.domain.common.resource.stat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 知识点题目统计(这里新旧知识点都需要统计)
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "knowledge_question_stat")
public class KnowledgeQuestionStat implements Serializable {

	private static final long serialVersionUID = 7690935731425540575L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 知识点代码
	 */
	@Column(name = "knowpoint_code")
	private long knowpointCode;

	/**
	 * 知识点名称
	 */
	@Column(name = "name", length = 128)
	private String name;

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
	 * 已录入
	 */
	@Column(name = "input_count", precision = 7)
	private int inputCount;

	/**
	 * 已通过
	 */
	@Column(name = "pass_count", precision = 7)
	private int passCount;

	/**
	 * 一较通过
	 */
	@Column(name = "onepass_count", precision = 7)
	private int onepassCount;

	/**
	 * 未校验
	 */
	@Column(name = "nocheck_count", precision = 7)
	private int nocheckCount;

	/**
	 * 不通过
	 */
	@Column(name = "nopass_count", precision = 7)
	private int nopassCount;

	/**
	 * 版本(新知识点存2,旧知识点存1)
	 */
	@Column(name = "version", precision = 3)
	private int version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getInputCount() {
		return inputCount;
	}

	public void setInputCount(int inputCount) {
		this.inputCount = inputCount;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public int getOnepassCount() {
		return onepassCount;
	}

	public void setOnepassCount(int onepassCount) {
		this.onepassCount = onepassCount;
	}

	public int getNocheckCount() {
		return nocheckCount;
	}

	public void setNocheckCount(int nocheckCount) {
		this.nocheckCount = nocheckCount;
	}

	public int getNopassCount() {
		return nopassCount;
	}

	public void setNopassCount(int nopassCount) {
		this.nopassCount = nopassCount;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
