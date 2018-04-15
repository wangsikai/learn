package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 教辅预置内容-易错疑难
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_pc_falldiff")
public class TeachAssistPresetContentFallibleDifficult implements Serializable {

	private static final long serialVersionUID = -6437577220811791359L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 教辅预置内容ID
	 */
	@Column(name = "teachassist_presetcontent_id")
	private Long teachassistPresetcontentId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 500)
	private String name;

	/**
	 * 易错点辨析
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "analysis")
	private String analysis;

	/**
	 * 针对性训练题目列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "targeted_training_questions")
	private List<Long> targetedTrainingQuestions;

	/**
	 * 校验状态
	 */
	@Column(name = "check_status", precision = 3, nullable = false)
	private CardStatus checkStatus = CardStatus.EDITING;

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

	public Long getTeachassistPresetcontentId() {
		return teachassistPresetcontentId;
	}

	public void setTeachassistPresetcontentId(Long teachassistPresetcontentId) {
		this.teachassistPresetcontentId = teachassistPresetcontentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public List<Long> getTargetedTrainingQuestions() {
		return targetedTrainingQuestions;
	}

	public void setTargetedTrainingQuestions(List<Long> targetedTrainingQuestions) {
		this.targetedTrainingQuestions = targetedTrainingQuestions;
	}

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
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
