package com.lanking.cloud.domain.common.resource.specialTraining;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 专项训练操作log
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "special_training_operate_log")
public class SpecialTrainingOperateLog implements Serializable {

	private static final long serialVersionUID = -6802665261113156782L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 专项训练ID
	 */
	@Column(name = "special_training_id")
	private Long specialTrainingId;

	/**
	 * 操作类型
	 * 
	 * @see SpecialTrainingOperateType
	 */
	@Column(name = "type", precision = 3, nullable = true)
	private SpecialTrainingOperateType operateType;

	/**
	 * 参数-可以存放被操作的题目ID等
	 */
	@Column(name = "p1", length = 128)
	private String p1;

	/**
	 * 创建人ID-操作人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间-操作时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpecialTrainingId() {
		return specialTrainingId;
	}

	public void setSpecialTrainingId(Long specialTrainingId) {
		this.specialTrainingId = specialTrainingId;
	}

	public SpecialTrainingOperateType getOperateType() {
		return operateType;
	}

	public void setOperateType(SpecialTrainingOperateType operateType) {
		this.operateType = operateType;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
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

}
