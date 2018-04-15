package com.lanking.cloud.domain.common.resource.book;

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
 * 书本操作历史
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "book_history")
public class BookHistory implements Serializable {
	private static final long serialVersionUID = -4320963925646885434L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 书本ID
	 */
	@Column(name = "book_id")
	private Long bookId;

	/**
	 * 操作状态
	 * 
	 * @see OperateType
	 */
	@Column(name = "type", precision = 3, nullable = true)
	private OperateType type;

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
	 * 操作的书本版本
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
		 * 发布.
		 */
		PUBLISH(3);

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

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
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
