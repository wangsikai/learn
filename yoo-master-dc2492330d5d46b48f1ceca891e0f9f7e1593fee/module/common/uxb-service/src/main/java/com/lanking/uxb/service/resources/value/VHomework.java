package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VClazz;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroup;

/**
 * 作业VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月17日
 */
public class VHomework implements Serializable {

	private static final long serialVersionUID = 3097126094771311730L;

	private long id;
	private long exerciseId;
	private long courseId;
	private long homeworkClazzId;
	private String name;
	private Date startTime;
	private Date deadline;
	// 到截止时间的毫秒数
	private long deadTime = 0;
	private Date createAt;
	/**
	 * @since 小优快批 （废弃）2018-02-11
	 */
	@Deprecated
	private Date issueAt;

	private HomeworkStatus status;
	/**
	 * @since 小优快批 （废弃）2018-02-11
	 */
	@Deprecated
	private String statusName;
	private HomeworkCorrectingType correctingType;

	private int rightCount;
	private int wrongCount;
	// 半错的数量
	private int halfWrongCount;

	private BigDecimal rightRate;
	private String rightRateTitle;
	private int homeworkTime;
	private long questionCount = 0;
	/**
	 * 分发数量
	 * 
	 * @since 2.1
	 */
	private long distributeCount = 0;
	/**
	 * 主动提交数量
	 * 
	 * @since 2.1
	 */
	private long commitCount = 0;
	/**
	 * 已经批改的数量
	 * 
	 * @since 2.1
	 */
	private long correctingCount = 0;

	/**
	 * 难度
	 * 
	 * @since 2.1
	 */
	private BigDecimal difficulty;

	/**
	 * 班级信息
	 * 
	 * @since 2.1
	 */
	private VClazz clazz;

	/**
	 * 教材.
	 * 
	 * @since 2.1
	 */
	private Integer textbookCode;
	// 元知识点
	private List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();

	/**
	 * 新知识点
	 * 
	 * @since yoomath v2.1.2 wanlong.che 2016-11-23
	 */
	private List<VKnowledgePoint> knowledgePoints = Lists.newArrayList();

	// 作业班级
	private VHomeworkClazz homeworkClazz;

	// 每道题的正确率
	private List<VHomeworkQuestion> homeworkQuestions;
	// 是否已经全部提交后X分钟
	private boolean allCommitMement = false;

	// 章节名称（作业篮子章节名称为：在线题库）
	private String sectionName;
	// 寒假作业时，返回作业完成率
	private BigDecimal completionRate;
	// 1标记 普通作业,2是假期作业
	private Integer type = 1;

	private Long sectionCode;

	/**
	 * 控制下发按钮显示（悠数学微信端使用）
	 */
	private boolean showIssue = true;

	// 是否有简答题
	private boolean hasQuestionAnswering = false;

	private Status delStatus;

	/**
	 * 是否自动下发 
	 * @since 小优快批 （废弃）2018-02-11
	 */
	@Deprecated
	private boolean autoIssue = false;
	// 作业限制时间
	private Integer timeLimit;
	// 教师名称
	private String teacherName;

	/**
	 * 如果作业布置给小组就有，不是小组没有<br>
	 * 2017.11.13新增
	 */
	private VHomeworkClazzGroup group;
	
	/**
	 * v1.5.3新增作业状态 0:待分发,1:作业中,2:已截止
	 * @since 2018-2-7
	 */
	private String newHomeworkStatus;
	private String newHomeworkStatusName;
	/**
	 * v1.5.3是否待批改 true:是,false:否
	 * @since 2018-2-7
	 */
	private boolean tobeCorrected = false;
	/**
	 * 作业预估时间
	 */
	private int homeworkPredictTime;
	/**
	 * 留言
	 */
	private List<VHomeworkMessage> messages;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public long getHomeworkClazzId() {
		return homeworkClazzId;
	}

	public void setHomeworkClazzId(long homeworkClazzId) {
		this.homeworkClazzId = homeworkClazzId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getDeadTime() {
		this.deadTime = getDeadline().getTime() - System.currentTimeMillis();
		return deadTime > 0 ? deadTime : 0;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getIssueAt() {
		return issueAt;
	}

	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public String getStatusName() {
		if (StringUtils.isBlank(statusName)) {
			String sn = "";
			if (getStatus() == HomeworkStatus.INIT) {
				sn = "待分发";
			} else if (getStatus() == HomeworkStatus.PUBLISH) {
				sn = "作业中";
			} else if (getStatus() == HomeworkStatus.NOT_ISSUE) {
				sn = "待批改";
			} else if (getStatus() == HomeworkStatus.ISSUED) {
				sn = "已下发";
			}
			setStatusName(sn);
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public HomeworkCorrectingType getCorrectingType() {
		return correctingType;
	}

	public void setCorrectingType(HomeworkCorrectingType correctingType) {
		this.correctingType = correctingType;
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

	public int getHalfWrongCount() {
		return halfWrongCount;
	}

	public void setHalfWrongCount(int halfWrongCount) {
		this.halfWrongCount = halfWrongCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(String.valueOf(getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public int getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(int homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public VClazz getClazz() {
		return clazz;
	}

	public void setClazz(VClazz clazz) {
		this.clazz = clazz;
	}

	public long getDistributeCount() {
		return distributeCount;
	}

	public void setDistributeCount(long distributeCount) {
		this.distributeCount = distributeCount;
	}

	public long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(long commitCount) {
		this.commitCount = commitCount;
	}

	public long getCorrectingCount() {
		return correctingCount;
	}

	public void setCorrectingCount(long correctingCount) {
		this.correctingCount = correctingCount;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public List<VMetaKnowpoint> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<VMetaKnowpoint> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public VHomeworkClazz getHomeworkClazz() {
		return homeworkClazz;
	}

	public void setHomeworkClazz(VHomeworkClazz homeworkClazz) {
		this.homeworkClazz = homeworkClazz;
	}

	public List<VHomeworkQuestion> getHomeworkQuestions() {
		return homeworkQuestions;
	}

	public void setHomeworkQuestions(List<VHomeworkQuestion> homeworkQuestions) {
		this.homeworkQuestions = homeworkQuestions;
	}

	public boolean isAllCommitMement() {
		return allCommitMement;
	}

	public void setAllCommitMement(boolean allCommitMement) {
		this.allCommitMement = allCommitMement;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean isShowIssue() {
		return showIssue;
	}

	public void setShowIssue(boolean showIssue) {
		this.showIssue = showIssue;
	}

	public boolean isHasQuestionAnswering() {
		return hasQuestionAnswering;
	}

	public void setHasQuestionAnswering(boolean hasQuestionAnswering) {
		this.hasQuestionAnswering = hasQuestionAnswering;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public List<VKnowledgePoint> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<VKnowledgePoint> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	/**
	 * @since 小优快批 （废弃）2018-02-11
	 */
	@Deprecated
	public boolean isAutoIssue() {
		return autoIssue;
	}

	/**
	 * @since 小优快批 （废弃）2018-02-11
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

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public VHomeworkClazzGroup getGroup() {
		return group;
	}

	public void setGroup(VHomeworkClazzGroup group) {
		this.group = group;
	}

	public String getNewHomeworkStatus() {
		return newHomeworkStatus;
	}

	public void setNewHomeworkStatus(String newHomeworkStatus) {
		this.newHomeworkStatus = newHomeworkStatus;
	}

	/**
	 * @since 小优快批 2018-02-11
	 * @return
	 */
	public String getNewHomeworkStatusName() {
		if (StringUtils.isBlank(newHomeworkStatusName)) {
			String sn = "";
			if (getNewHomeworkStatus() == "0") {
				sn = "待分发";
			} else if (getNewHomeworkStatus() == "1") {
				sn = "作业中";
			} else if (getNewHomeworkStatus() == "2") {
				sn = "已截止";
			} 
			setNewHomeworkStatusName(sn);
		}
		return newHomeworkStatusName;
	}

	public void setNewHomeworkStatusName(String newHomeworkStatusName) {
		this.newHomeworkStatusName = newHomeworkStatusName;
	}

	public boolean isTobeCorrected() {
		return tobeCorrected;
	}

	public void setTobeCorrected(boolean tobeCorrected) {
		this.tobeCorrected = tobeCorrected;
	}

	public int getHomeworkPredictTime() {
		return homeworkPredictTime;
	}

	public void setHomeworkPredictTime(int homeworkPredictTime) {
		this.homeworkPredictTime = homeworkPredictTime;
	}

	public List<VHomeworkMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<VHomeworkMessage> messages) {
		this.messages = messages;
	}
}
