package com.lanking.uxb.service.zuoye.form;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;

/**
 * 布置作业
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public class PublishHomeworkForm {
	// 预置练习ID
	private Long textbookExerciseId;
	private Long sectionCode;
	// 已经有的题目ID列表
	private List<Long> qIds;
	// 课程ID列表
	private List<Long> courseIds;
	// 作业班级ID列表
	private List<Long> homeworkClassIds;
	// 开始时间
	private Long startTime;
	// 结束时间
	private Long deadline;
	// 创建人,在control中覆盖
	private Long createId;
	// 难度
	private BigDecimal difficulty;
	// 作业批改类型
	private HomeworkCorrectingType correctingType = HomeworkCorrectingType.TEACHER;

	// 包含的知识点
	private List<Long> metaKnowpoints = Lists.newArrayList();

	/**
	 * 新知识点 v2.1.2 wanlong.che 2016-11-24
	 */
	private List<Long> knowledgePoints = Lists.newArrayList();

	// 作业名称,如果有textbookExerciseId的情况下此参数无效
	private String name;
	// 从作业篮子布置作业
	private boolean fromCar = false;
	// 是否订正上一次作业的错题(此参数传参无效,在controller里面从配置文件里面获取)
	private boolean correctLastHomework = false;
	/**
	 * 关联的书本ID,如果从书本中布置作业时,sectionCode为书本的章节ID
	 * 
	 * @since yoomath V1.6
	 */
	private Long bookId;
	// 通过此作业再次布置
	private Long homeworkId;

	/**
	 * 自动下发标记（默认不自动下发）.
	 * 
	 * @since yoomath v2.3.0
	 */
	@Deprecated
	private boolean autoIssue = false;

	/**
	 * 通过题目查询到的新知识点
	 * 
	 * @since 教师端 v1.3.0
	 */
	private List<Long> questionknowledgePoints = Lists.newArrayList();

	/**
	 * 作业限时时间（单位分钟）
	 * 
	 * @since 教师端 v1.3.0
	 */
	private Integer timeLimit;

	/**
	 * 
	 * 新增分组布置作业 2017.11.8
	 * 
	 * @author wangsenhao
	 * 
	 *         <pre>
	 * [
	 * 	{
	 * 		classId:11,
	 * 	    groupId:1
	 *  },
	 *  {
	 * 		classId:12
	 *  },
	 *  {
	 * 		classId:13,
	 * 	    groupId:2
	 *  }
	 *  ...
	 * ]
	 * </pre>
	 */
	private String classGroupList;

	/**
	 * 解答题自动批改标记.
	 * 
	 * @since 小优快批
	 */
	private boolean answerQuestionAutoCorrect = false;
	public Long getTextbookExerciseId() {
		return textbookExerciseId;
	}

	public void setTextbookExerciseId(Long textbookExerciseId) {
		this.textbookExerciseId = textbookExerciseId;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public List<Long> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(List<Long> courseIds) {
		this.courseIds = courseIds;
	}

	public List<Long> getHomeworkClassIds() {
		return homeworkClassIds;
	}

	public void setHomeworkClassIds(List<Long> homeworkClassIds) {
		this.homeworkClassIds = homeworkClassIds;
	}

	public Long getStartTime() {
		if (startTime == null) {
			return System.currentTimeMillis();
		}
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getDeadline() {
		return deadline;
	}

	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public HomeworkCorrectingType getCorrectingType() {
		return correctingType;
	}

	public void setCorrectingType(HomeworkCorrectingType correctingType) {
		this.correctingType = correctingType;
	}

	public List<Long> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Long> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFromCar() {
		return fromCar;
	}

	public void setFromCar(boolean fromCar) {
		this.fromCar = fromCar;
	}

	public boolean isCorrectLastHomework() {
		return correctLastHomework;
	}

	public void setCorrectLastHomework(boolean correctLastHomework) {
		this.correctLastHomework = correctLastHomework;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}
	@Deprecated
	public boolean getAutoIssue() {
		return autoIssue;
	}
	@Deprecated
	public void setAutoIssue(boolean autoIssue) {
		this.autoIssue = autoIssue;
	}

	public List<Long> getQuestionknowledgePoints() {
		return questionknowledgePoints;
	}

	public void setQuestionknowledgePoints(List<Long> questionknowledgePoints) {
		this.questionknowledgePoints = questionknowledgePoints;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getClassGroupList() {
		return classGroupList;
	}

	public void setClassGroupList(String classGroupList) {
		this.classGroupList = classGroupList;
	}

	public boolean isAnswerQuestionAutoCorrect() {
		return answerQuestionAutoCorrect;
	}

	public void setAnswerQuestionAutoCorrect(boolean answerQuestionAutoCorrect) {
		this.answerQuestionAutoCorrect = answerQuestionAutoCorrect;
	}



}
