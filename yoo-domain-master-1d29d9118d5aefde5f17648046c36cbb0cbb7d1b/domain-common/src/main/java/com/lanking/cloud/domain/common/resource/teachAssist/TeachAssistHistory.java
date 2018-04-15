package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 教辅操作历史
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_history")
public class TeachAssistHistory implements Serializable {

	private static final long serialVersionUID = -8379747458721783908L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 教辅ID
	 */
	@Column(name = "teachassist_id")
	private Long teachAssistId;

	/**
	 * 操作类型
	 */
	@Column(name = "type", precision = 3, nullable = true)
	private OperateType type;

	/**
	 * 操作人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 操作时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 操作版本
	 */
	@Column(name = "version")
	private Integer version;

	/**
	 * 操作类型
	 * 
	 * @since 3.9.3
	 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
	 * @version 2017年3月20日
	 */
	public enum OperateType implements Valueable {
		/**
		 * 未知
		 */
		NULL(0),
		/**
		 * 创建版本
		 */
		CREATE(1),
		/**
		 * 发布
		 */
		PUBLISH(2);

		private int value;

		OperateType(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			return value;
		}

		public static OperateType findByValue(int value) {
			switch (value) {
			case 0:
				return NULL;
			case 1:
				return CREATE;
			case 2:
				return PUBLISH;
			default:
				return NULL;
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeachAssistId() {
		return teachAssistId;
	}

	public void setTeachAssistId(Long teachAssistId) {
		this.teachAssistId = teachAssistId;
	}

	public OperateType getType() {
		return type;
	}

	public void setType(OperateType type) {
		this.type = type;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
