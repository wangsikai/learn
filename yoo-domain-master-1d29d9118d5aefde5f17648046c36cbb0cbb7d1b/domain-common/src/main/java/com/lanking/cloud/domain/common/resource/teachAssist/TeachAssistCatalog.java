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
import com.lanking.cloud.domain.common.resource.card.CardStatus;

/**
 * 教辅目录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_catalog")
public class TeachAssistCatalog implements Serializable {

	private static final long serialVersionUID = 6542240836379065884L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属教辅版本ID
	 */
	@Column(name = "teachassist_version_id")
	private Long teachassistVersionId;

	/**
	 * 目录名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 上级目录ID
	 */
	@Column(name = "pid")
	private Long pid;

	/**
	 * 层级
	 */
	@Column(name = "level", precision = 3)
	private int level;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private int sequence;

	/**
	 * 校验状态
	 */
	@Column(name = "check_status", precision = 3, nullable = false)
	private CardStatus checkStatus = CardStatus.EDITING;

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

	public Long getTeachassistVersionId() {
		return teachassistVersionId;
	}

	public void setTeachassistVersionId(Long teachassistVersionId) {
		this.teachassistVersionId = teachassistVersionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
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
