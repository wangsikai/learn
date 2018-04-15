package com.lanking.cloud.domain.yoomath.smartExamPaper;

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
 * 智能试卷
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "smart_exam_paper")
public class SmartExamPaper implements Serializable {

	private static final long serialVersionUID = 3485392200498881263L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 对应的试卷难度类型
	 */
	@Column(name = "smart_difficulty")
	private SmartExamPaperDifficulty smartExamPaperDifficulty;

	/**
	 * 包含题目的数量
	 */
	@Column(name = "question_count")
	private int questionCount;

	/**
	 * 平均难度
	 */
	@Column(name = "difficulty")
	private Double difficulty;

	/**
	 * 答对数量
	 */
	@Column(name = "right_count")
	private Integer rightCount;

	/**
	 * 答错数量
	 */
	@Column(name = "wrong_count")
	private Integer wrongCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private int textbookCode;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 提交时间
	 */
	@Column(name = "commit_at", columnDefinition = "datetime(3)")
	private Date commitAt;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private SmartPaperStatus status = SmartPaperStatus.NEWEST;

	/**
	 * 关联智能出卷ID,重新练习时用到
	 */
	@Column(name = "paper_id", columnDefinition = "bigint default 0")
	private long paperId = 0;

	/**
	 * 用时
	 */
	@Column(name = "homework_time", columnDefinition = "bigint default 0")
	private Integer homeworkTime = 0;

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public SmartExamPaperDifficulty getSmartExamPaperDifficulty() {
		return smartExamPaperDifficulty;
	}

	public void setSmartExamPaperDifficulty(SmartExamPaperDifficulty smartExamPaperDifficulty) {
		this.smartExamPaperDifficulty = smartExamPaperDifficulty;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getRightCount() {
		return rightCount;
	}

	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public int getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(int textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getCommitAt() {
		return commitAt;
	}

	public void setCommitAt(Date commitAt) {
		this.commitAt = commitAt;
	}

	public SmartPaperStatus getStatus() {
		return status;
	}

	public void setStatus(SmartPaperStatus status) {
		this.status = status;
	}

	public long getPaperId() {
		return paperId;
	}

	public void setPaperId(long paperId) {
		this.paperId = paperId;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

}
