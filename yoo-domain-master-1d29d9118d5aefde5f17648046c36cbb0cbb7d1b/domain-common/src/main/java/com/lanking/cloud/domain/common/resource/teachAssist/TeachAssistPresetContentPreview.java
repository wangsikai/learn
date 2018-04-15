package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 教辅预置内容-预习点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_pc_preview")
public class TeachAssistPresetContentPreview implements Serializable {

	private static final long serialVersionUID = -6935694929098019574L;

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
	 * 知识点
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "knowpoints")
	private List<Long> knowpoints = Lists.newArrayList();

	/**
	 * 情景预设题目列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "preview_questions")
	private List<Long> previewQuestions;

	/**
	 * 预习自测题目列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "selftest_questions")
	private List<Long> selfTestQuestions;

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

	public List<Long> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<Long> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public List<Long> getPreviewQuestions() {
		return previewQuestions;
	}

	public void setPreviewQuestions(List<Long> previewQuestions) {
		this.previewQuestions = previewQuestions;
	}

	public List<Long> getSelfTestQuestions() {
		return selfTestQuestions;
	}

	public void setSelfTestQuestions(List<Long> selfTestQuestions) {
		this.selfTestQuestions = selfTestQuestions;
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
