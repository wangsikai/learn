package com.lanking.cloud.domain.common.resource.examPaper;

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
 * 试卷操作记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "exam_paper_history")
public class ExamPaperHistory implements Serializable {
	private static final long serialVersionUID = 805716397349110424L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 试卷ID
	 */
	@Column(name = "exam_paper_id")
	private Long examPaperId;

	/**
	 * 操作状态
	 * 
	 * @see OperateType
	 */
	@Column(name = "type", precision = 3, nullable = true)
	private OperateType operateType;

	/**
	 * 操作人
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 操作时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 操作类型
	 * 
	 * @since 3.9.3
	 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
	 * @version 2017年3月20日
	 */
	public enum OperateType implements Valueable {
		/**
		 * 未知.
		 */
		NULL(0),
		/**
		 * 创建.
		 */
		CREATE(1),
		/**
		 * 编辑.
		 */
		EDIT(2),
		/**
		 * 提交.
		 */
		SUBMIT(3),
		/**
		 * 发布.
		 */
		PUBLISH(4),
		/**
		 * 重新编辑（发布之后的状态）.
		 */
		REEDIT(5);

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
				return EDIT;
			case 3:
				return SUBMIT;
			case 4:
				return PUBLISH;
			case 5:
				return REEDIT;
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

	public Long getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(Long examPaperId) {
		this.examPaperId = examPaperId;
	}

	public OperateType getOperateType() {
		return operateType;
	}

	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
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
