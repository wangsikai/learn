package com.lanking.cloud.domain.common.resource.specialTraining;

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

/**
 * 专项训练
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "special_training")
public class SpecialTraining implements Serializable {

	private static final long serialVersionUID = 5686168789196157855L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 128)
	private String name;

	/**
	 * 关联知识点的代码(目前只能管理专题即新知识体系的第二级)
	 */
	@Column(name = "knowpoint_code")
	private long knowpointCode;

	/**
	 * 关联知识点
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<Long> knowpoints = Lists.newArrayList();

	/**
	 * 关联考点
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<Long> examinationPoints = Lists.newArrayList();

	/**
	 * 难度
	 */
	@Column(name = "difficulty")
	private Double difficulty;

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

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private SpecialTrainingStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public List<Long> getKnowpoints() {
		return knowpoints;
	}

	public void setKnowpoints(List<Long> knowpoints) {
		this.knowpoints = knowpoints;
	}

	public List<Long> getExaminationPoints() {
		return examinationPoints;
	}

	public void setExaminationPoints(List<Long> examinationPoints) {
		this.examinationPoints = examinationPoints;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
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

	public SpecialTrainingStatus getStatus() {
		return status;
	}

	public void setStatus(SpecialTrainingStatus status) {
		this.status = status;
	}

}
