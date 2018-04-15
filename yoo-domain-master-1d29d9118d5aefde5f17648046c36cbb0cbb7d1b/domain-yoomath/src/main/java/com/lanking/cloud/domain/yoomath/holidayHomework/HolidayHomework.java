package com.lanking.cloud.domain.yoomath.holidayHomework;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 假日作业
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "holiday_homework")
public class HolidayHomework implements Serializable {

	private static final long serialVersionUID = 6375413926954574853L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 假日作业类型
	 */
	@Column(name = "type", precision = 3)
	private HolidayHomeworkType type;

	/**
	 * 班级ID
	 */
	@Column(name = "homework_class_id")
	private Long homeworkClassId;

	/**
	 * 作业名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 难度
	 */
	@Column(name = "difficulty")
	private BigDecimal difficulty;

	/**
	 * 题目数量
	 */
	@Column(name = "question_count")
	private Integer questionCount;

	/**
	 * 包含知识点
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<Long> metaKnowpoints;

	/**
	 * 包含新知识点
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "knowledge_points", length = 4000)
	private List<Long> knowledgePoints;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "deadline", columnDefinition = "datetime(3)")
	private Date deadline;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private long createId;

	/**
	 * 原始创建人(布置假期作业时需要写入此字段)
	 */
	@Column(name = "original_create_id")
	private Long originalCreateId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 作业状态
	 */
	@Column(name = "status", precision = 3)
	private HomeworkStatus status = HomeworkStatus.INIT;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3)
	private Status delStatus = Status.ENABLED;

	/**
	 * 作业平均用时
	 */
	@Column(name = "homework_time")
	private Integer homeworkTime = 0;

	/**
	 * 对题数量
	 */
	@Column(name = "right_count")
	private Integer rightCount;

	/**
	 * 错题数量
	 */
	@Column(name = "wrong_count")
	private Integer wrongCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 完成率
	 */
	@Column(name = "completion_rate", scale = 2)
	private BigDecimal completionRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public List<Long> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Long> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public Long getOriginalCreateId() {
		return originalCreateId;
	}

	public void setOriginalCreateId(Long originalCreateId) {
		this.originalCreateId = originalCreateId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
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

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

}
