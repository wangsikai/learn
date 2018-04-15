package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 教辅预置内容
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_presetcontent")
public class TeachAssistPresetContent implements Serializable {

	private static final long serialVersionUID = 7914796898501015536L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 知识点专项代码
	 */
	@Column(name = "knowledge_system_code")
	private Long knowledgeSystemCode;

	/**
	 * 学习目标
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "learning_goals")
	private String learningGoals;

	/**
	 * 解题方法
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "solving_method")
	private String solvingMethod;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3, nullable = false)
	private Status delStatus = Status.ENABLED;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getKnowledgeSystemCode() {
		return knowledgeSystemCode;
	}

	public void setKnowledgeSystemCode(Long knowledgeSystemCode) {
		this.knowledgeSystemCode = knowledgeSystemCode;
	}

	public String getLearningGoals() {
		return learningGoals;
	}

	public void setLearningGoals(String learningGoals) {
		this.learningGoals = learningGoals;
	}

	public String getSolvingMethod() {
		return solvingMethod;
	}

	public void setSolvingMethod(String solvingMethod) {
		this.solvingMethod = solvingMethod;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
