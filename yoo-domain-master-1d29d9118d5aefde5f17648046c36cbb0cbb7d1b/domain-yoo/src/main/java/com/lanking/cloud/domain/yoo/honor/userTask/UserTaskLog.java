package com.lanking.cloud.domain.yoo.honor.userTask;

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
 * 用户任务记录
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
@Entity
@Table(name = "user_task_log")
public class UserTaskLog implements Serializable {

	private static final long serialVersionUID = -1702507996305163969L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 任务代码
	 */
	@Column(name = "task_code", length = 9)
	private Integer taskCode;

	/**
	 * 任务类型
	 */
	@Column(name = "task_type", precision = 3)
	private UserTaskType taskType;

	/**
	 * 创建时间,只保存年月日
	 */
	@Column(name = "create_at", columnDefinition = "date")
	private Date createAt;

	/**
	 * 字段无效时请记录为-1
	 */
	@Column(name = "coins", precision = 5)
	private int coins;

	/**
	 * 字段无效时请记录为-1
	 */
	@Column(name = "growth", precision = 5)
	private int growth;

	/**
	 * 字段无效时请记录为-1
	 */
	@Column(name = "star", precision = 5)
	private int star;

	/**
	 * 任务完成详细记录(JSONOBJECT),每种类型需要加注释
	 * 
	 * <pre>
	 * 新手任务:
	 * 每日任务:
	 * 成就任务:
	 * </pre>
	 */
	@Column(name = "content", length = 4000)
	private String content;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private UserTaskLogStatus status;

	/**
	 * 最后更新时间
	 */
	@Column(name = "complete_at", columnDefinition = "datetime(3)")
	private Date completeAt;

	/**
	 * 领取时间
	 */
	@Column(name = "receive_at", columnDefinition = "datetime(3)")
	private Date receiveAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Integer getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(Integer taskCode) {
		this.taskCode = taskCode;
	}

	public UserTaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(UserTaskType taskType) {
		this.taskType = taskType;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UserTaskLogStatus getStatus() {
		return status;
	}

	public void setStatus(UserTaskLogStatus status) {
		this.status = status;
	}

	public Date getCompleteAt() {
		return completeAt;
	}

	public void setCompleteAt(Date completeAt) {
		this.completeAt = completeAt;
	}

	public Date getReceiveAt() {
		return receiveAt;
	}

	public void setReceiveAt(Date receiveAt) {
		this.receiveAt = receiveAt;
	}
}