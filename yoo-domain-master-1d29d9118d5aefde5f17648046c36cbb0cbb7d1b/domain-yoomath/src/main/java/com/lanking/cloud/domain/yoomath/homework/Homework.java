package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.LongTypeList;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;

/**
 * 作业
 * 
 * @since 3.9.3
 * @since 学生端v1.3.4 2017-5-27 添加初始化开关
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "homework")
@TypeDef(name = "list", defaultForType = List.class, typeClass = LongTypeList.class)
public class Homework implements Serializable {

	private static final long serialVersionUID = -5400042679564324943L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联练习ID
	 */
	@Column(name = "exercise_id")
	private Long exerciseId;

	/**
	 * 课程ID
	 * 
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 */
	@Deprecated
	@Column(name = "course_id")
	private Long courseId;

	/**
	 * 班级ID
	 */
	@Column(name = "homework_class_id")
	private Long homeworkClassId;

	/**
	 * 班级分组ID
	 */
	@Column(name = "homework_class_group_id")
	private Long homeworkClassGroupId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal rightRate;

	/**
	 * 作业平均用时
	 */
	@Column(name = "homework_time", columnDefinition = "bigint default 0")
	private Integer homeworkTime = 0;

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
	 * 创建人ID(转让后需要将非下发状态的作业此字段更新为当前的老师ID)
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 原始创建人(布置作业时需要写入此字段)
	 */
	@Column(name = "original_create_id")
	private Long originalCreateId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 下发时间
	 * 
	 * @since 小优快批
	 *        2018-02-11，该字段不再表示下发时间，仅表示为作业全部批改完成的时间，见all_correct_complete字段.
	 */
	@Column(name = "issue_at", columnDefinition = "datetime(3)")
	private Date issueAt;

	/**
	 * 最后一个提交时间
	 */
	@Column(name = "last_commit_at", columnDefinition = "datetime(3)")
	private Date lastCommitAt;

	/**
	 * 作业状态
	 */
	@Column(precision = 3, nullable = false)
	private HomeworkStatus status = HomeworkStatus.INIT;

	/**
	 * 批改类型
	 */
	@Column(name = "correcting_type", precision = 3, columnDefinition = "tinyint default 0")
	private HomeworkCorrectingType correctingType = HomeworkCorrectingType.TEACHER;

	/**
	 * 教材
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 作业分发的数量
	 */
	@Column(name = "distribute_count", columnDefinition = "bigint default 0")
	private Long distributeCount = 0L;

	/**
	 * 作业提交的数量(有效提交的数量，包括有效主动提交和有效自动提交)
	 */
	@Column(name = "commit_count", columnDefinition = "bigint default 0")
	private Long commitCount = 0L;

	/**
	 * 作业批改的数量
	 * 
	 * @since 小优快批，2018-2-26，新流程中记录学生作业批改状态为complete的数量
	 */
	@Column(name = "correcting_count", columnDefinition = "bigint default 0")
	private Long correctingCount = 0L;

	/**
	 * 作业里面的题目数量
	 */
	@Column(name = "question_count", columnDefinition = "bigint default 0")
	private Long questionCount = 0L;

	/**
	 * 作业难度
	 */
	@Column(name = "difficulty")
	private BigDecimal difficulty;

	/**
	 * 答对题目的数量
	 */
	@Column(name = "right_count")
	private Integer rightCount;

	/**
	 * 答错题目的数量
	 */
	@Column(name = "wrong_count")
	private Integer wrongCount;

	/**
	 * 半错数量
	 */
	@Column(name = "half_wrong_count")
	private Integer halfWrongCount;

	/**
	 * 旧知识点
	 */
	@Type(type = "list")
	@Column(name = "meta_knowpoints", length = 4000)
	private List<Long> metaKnowpoints;

	/**
	 * 上一个下发的并且未被订正的作业ID(布置作业的时候记录)
	 * 
	 * @since 小优快批，2018-2-27，订正题不再通过上份作业获得
	 */
	@Deprecated
	@Column(name = "last_issued")
	private Long lastIssued;

	/**
	 * 最后一个作业提交后将此字段设置为true,并且会做相关统计
	 */
	@Column(name = "after_lastcommit_stat", columnDefinition = "bit default 0")
	private boolean afterLastcommitStat;

	/**
	 * 是否被订正过
	 */
	@Column(name = "corrected", columnDefinition = "bit default 0")
	private boolean corrected = false;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3, columnDefinition = "tinyint default 0")
	private Status delStatus = Status.ENABLED;

	/**
	 * 管理状态(后台使用)
	 */
	@Column(name = "man_status", precision = 3, columnDefinition = "tinyint default 0")
	private Status manStatus = Status.ENABLED;

	/**
	 * 是否有简答题
	 */
	@Column(name = "has_question_answering", columnDefinition = "bit default 0")
	private boolean hasQuestionAnswering = false;

	/**
	 * 新知识点
	 */
	@Type(type = "list")
	@Column(name = "knowledge_points", length = 4000)
	private List<Long> knowledgePoints;

	/**
	 * 通过题目查询到的新知识点,knowledge_points为空时这个字段需要存<br>
	 * 20170511新增 senhao.wang
	 * 
	 */
	@Type(type = "list")
	@Column(name = "question_knowledge_points", length = 4000)
	private List<Long> questionknowledgePoints;

	/**
	 * 是否自动下发标记，默认关闭
	 * 
	 * @since 小优快批（废弃） 2018-02-11
	 */
	@Column(name = "auto_issue", columnDefinition = "bit default 0")
	@Deprecated
	private boolean autoIssue = false;

	/**
	 * 作业限时时间（单位分钟,默认为null）
	 */
	@Column(name = "time_limit", precision = 5)
	private Integer timeLimit;

	/**
	 * 解答题自动批改标记，默认关闭.
	 * 
	 * @since 小优快批
	 */
	@Column(name = "aq_auto_correct", columnDefinition = "bit default 0")
	private boolean answerQuestionAutoCorrect = false;

	/**
	 * 待批改标记.
	 * <p>
	 * true 表示待批改
	 * </p>
	 * 
	 * @since 小优快批
	 */
	@Column(name = "tobe_corrected")
	private boolean tobeCorrected = false;

	/**
	 * 全部批改完成标记.
	 * <p>
	 * true 表示所有学生作业都被批改完毕（非订正题，除了学生未做答被强制提交的作业）.
	 * </p>
	 * 
	 * @since 小优快批
	 */
	@Column(name = "all_correct_complete")
	private boolean allCorrectComplete = false;

	/**
	 * 是否已经统计过整份作业的正确率.
	 */
	@Column(name = "right_rate_stat_flag")
	private Boolean rightRateStatFlag = false;
	
	@Transient
	private boolean initCount = false; // 是否初始化作业统计
	@Transient
	private long studentHomeworkId;
	@Transient
	private Exercise exercise;

	// since 2017-5-27
	@Transient
	private boolean initExercise = true; // 初始化作业对应的练习（兼容设置，默认true）
	@Transient
	private boolean initSectionOrBookCatalog = true; // 初始化章节信息（兼容设置，默认true）
	@Transient
	private boolean initMetaKnowpoint = true; // 初始化旧知识点（兼容设置，默认true）
	@Transient
	private boolean initKnowledgePoint = true; // 初始化新知识点（兼容设置，默认true）

	// since 2017-12-15 增加可选项
	@Transient
	private boolean initTeacherName = true; // 初始化教师姓名（兼容设置，默认true）

	/**
	 * 是否查询留言信息
	 */
	@Transient
	private boolean initMessages = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	/**
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 * @return
	 */
	@Deprecated
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * @since 小优快批，2018-3-9，业务已不需要该字段，废弃
	 * @param courseId
	 */
	@Deprecated
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public Long getHomeworkClassGroupId() {
		return homeworkClassGroupId;
	}

	public void setHomeworkClassGroupId(Long homeworkClassGroupId) {
		this.homeworkClassGroupId = homeworkClassGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
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

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
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

	/**
	 * @since 小优快批
	 *        2018-02-11，该字段不再表示下发时间，仅表示为作业全部批改完成的时间，见all_correct_complete字段.
	 * @return
	 */
	public Date getIssueAt() {
		return issueAt;
	}

	/**
	 * @since 小优快批
	 *        2018-02-11，该字段不再表示下发时间，仅表示为作业全部批改完成的时间，见all_correct_complete字段.
	 * @return
	 */
	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}

	public Date getLastCommitAt() {
		return lastCommitAt;
	}

	public void setLastCommitAt(Date lastCommitAt) {
		this.lastCommitAt = lastCommitAt;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public HomeworkCorrectingType getCorrectingType() {
		return correctingType;
	}

	public void setCorrectingType(HomeworkCorrectingType correctingType) {
		this.correctingType = correctingType;
	}

	public Long getDistributeCount() {
		return distributeCount;
	}

	public void setDistributeCount(Long distributeCount) {
		this.distributeCount = distributeCount;
	}

	public Long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(Long commitCount) {
		this.commitCount = commitCount;
	}

	public Long getCorrectingCount() {
		return correctingCount;
	}

	public void setCorrectingCount(Long correctingCount) {
		this.correctingCount = correctingCount;
	}

	public Long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Long questionCount) {
		this.questionCount = questionCount;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
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

	public Integer getHalfWrongCount() {
		return halfWrongCount;
	}

	public void setHalfWrongCount(Integer halfWrongCount) {
		this.halfWrongCount = halfWrongCount;
	}

	public List<Long> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Long> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	@Deprecated
	public Long getLastIssued() {
		return lastIssued;
	}

	@Deprecated
	public void setLastIssued(Long lastIssued) {
		this.lastIssued = lastIssued;
	}

	public boolean isAfterLastcommitStat() {
		return afterLastcommitStat;
	}

	public void setAfterLastcommitStat(boolean afterLastcommitStat) {
		this.afterLastcommitStat = afterLastcommitStat;
	}

	public boolean isCorrected() {
		return corrected;
	}

	public void setCorrected(boolean corrected) {
		this.corrected = corrected;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public boolean isInitCount() {
		return initCount;
	}

	public void setInitCount(boolean initCount) {
		this.initCount = initCount;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	// 是否是课程作业
	public boolean isCourse() {
		return getCourseId() != null && getCourseId() > 0;
	}

	public long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public Status getManStatus() {
		return manStatus;
	}

	public void setManStatus(Status manStatus) {
		this.manStatus = manStatus;
	}

	public boolean isHasQuestionAnswering() {
		return hasQuestionAnswering;
	}

	public void setHasQuestionAnswering(boolean hasQuestionAnswering) {
		this.hasQuestionAnswering = hasQuestionAnswering;
	}

	public List<Long> getKnowledgePoints() {
		if (CollectionUtils.isEmpty(knowledgePoints)) {
			return questionknowledgePoints;
		}
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	/**
	 * @since 小优快批（废弃） 2018-02-11
	 * @return
	 */
	@Deprecated
	public boolean isAutoIssue() {
		return autoIssue;
	}

	/**
	 * @since 小优快批（废弃） 2018-02-11
	 * @return
	 */
	@Deprecated
	public void setAutoIssue(boolean autoIssue) {
		this.autoIssue = autoIssue;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public List<Long> getQuestionknowledgePoints() {
		return questionknowledgePoints;
	}

	public void setQuestionknowledgePoints(List<Long> questionknowledgePoints) {
		this.questionknowledgePoints = questionknowledgePoints;
	}

	public boolean isInitExercise() {
		return initExercise;
	}

	public void setInitExercise(boolean initExercise) {
		this.initExercise = initExercise;
	}

	public boolean isInitSectionOrBookCatalog() {
		return initSectionOrBookCatalog;
	}

	public void setInitSectionOrBookCatalog(boolean initSectionOrBookCatalog) {
		this.initSectionOrBookCatalog = initSectionOrBookCatalog;
	}

	public boolean isInitMetaKnowpoint() {
		return initMetaKnowpoint;
	}

	public void setInitMetaKnowpoint(boolean initMetaKnowpoint) {
		this.initMetaKnowpoint = initMetaKnowpoint;
	}

	public boolean isInitKnowledgePoint() {
		return initKnowledgePoint;
	}

	public void setInitKnowledgePoint(boolean initKnowledgePoint) {
		this.initKnowledgePoint = initKnowledgePoint;
	}

	public boolean isInitTeacherName() {
		return initTeacherName;
	}

	public void setInitTeacherName(boolean initTeacherName) {
		this.initTeacherName = initTeacherName;
	}

	public boolean isAnswerQuestionAutoCorrect() {
		return answerQuestionAutoCorrect;
	}

	public void setAnswerQuestionAutoCorrect(boolean answerQuestionAutoCorrect) {
		this.answerQuestionAutoCorrect = answerQuestionAutoCorrect;
	}

	public boolean isTobeCorrected() {
		return tobeCorrected;
	}

	public void setTobeCorrected(boolean tobeCorrected) {
		this.tobeCorrected = tobeCorrected;
	}

	public boolean isAllCorrectComplete() {
		return allCorrectComplete;
	}

	public void setAllCorrectComplete(boolean allCorrectComplete) {
		this.allCorrectComplete = allCorrectComplete;
	}

	public boolean isInitMessages() {
		return initMessages;
	}

	public void setInitMessages(boolean initMessages) {
		this.initMessages = initMessages;
	}

	public Boolean getRightRateStatFlag() {
		return rightRateStatFlag;
	}

	public void setRightRateStatFlag(Boolean rightRateStatFlag) {
		this.rightRateStatFlag = rightRateStatFlag;
	}

}
