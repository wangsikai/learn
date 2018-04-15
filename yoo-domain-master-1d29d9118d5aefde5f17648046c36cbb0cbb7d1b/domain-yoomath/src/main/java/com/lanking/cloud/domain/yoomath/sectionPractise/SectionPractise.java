package com.lanking.cloud.domain.yoomath.sectionPractise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 章节练习
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "section_practise")
public class SectionPractise implements Serializable {
	private static final long serialVersionUID = 3651341676869780946L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 包含题目数量
	 */
	@Column(name = "question_count")
	private int questionCount;

	/**
	 * 已做的题目数量
	 */
	@Column(name = "do_count")
	private int doCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate")
	private BigDecimal rightRate;

	/**
	 * 正确题目数量
	 */
	@Column(name = "right_count")
	private int rightCount;

	/**
	 * 错误题目数量
	 */
	@Column(name = "wrong_count")
	private int wrongCount;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 提交时间
	 */
	@Column(name = "commit_at", columnDefinition = "datetime(3)")
	private Date commitAt;

	/**
	 * 难度
	 */
	@Column(name = "difficulty")
	private Double difficulty;

	/**
	 * 对应哪个章节下的练习，章节代码
	 */
	@Column(name = "section_code")
	private long sectionCode;

	/**
	 * 练习使用时间
	 */
	@Column(name = "homework_time", columnDefinition = "bigint default 0")
	private Integer homeworkTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public int getDoCount() {
		return doCount;
	}

	public void setDoCount(int doCount) {
		this.doCount = doCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Date getCommitAt() {
		return commitAt;
	}

	public void setCommitAt(Date commitAt) {
		this.commitAt = commitAt;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}
}
