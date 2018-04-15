package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;

/**
 * 学生作业VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月19日
 */
public class VStudentHomework implements Serializable {

	private static final long serialVersionUID = 2894559048867065793L;

	private long id;
	private long studentId;
	private VUser user;
	private VHomeworkStudentClazz studentClazz;

	private long homeworkId;
	private Date createAt;
	private Date submitAt;
	private Date stuSubmitAt;
	private Date issueAt;
	private StudentHomeworkStatus status;
	private String statusName;

	/**
	 * 此属性为有没有批改完成
	 * 
	 * @since 小优快批，2018-2-12，新流程判断不再使用该字段，切勿使用！
	 */
	@Deprecated
	private boolean corrected;

	/**
	 * 此属性为学生有没有批改完成
	 * 
	 * @since 小优快批，2018-2-12，新流程判断不再使用该字段，切勿使用！
	 */
	@Deprecated
	private boolean studentCorrected = true;

	private BigDecimal rightRate;
	private String rightRateTitle;
	
	//订正后的正确率
	private BigDecimal rightRateCorrect;
	private String rightRateCorrectTitle;
	
	private int homeworkTime;
	private int rank;
	private int rightCount;
	private int wrongCount;
	// 半错的数量
	private int halfWrongCount;

	// 单选多选判断填空有没有都被批改过,如果都被批改过，此时前台老师可以批改简答题(人工智能批改完)
	/**
	 * @since 小悠快批 2018-2-11 不再使用该字段判断，教师是否可以批改应该判断correct_status字段
	 */
	@Deprecated
	private boolean autoManualAllCorrected = false;

	private BigDecimal completionRate;
	private String completionRateTitle;
	// 已经完成的数量
	private int completionCount = 0;

	private Date updateAt;

	private VHomework homework;

	// 是否超过限时时间
	private boolean timeout = false;

	// 总的订正题目数量
	private Integer revisalQuestionTotal;

	// 已订正题目数量
	private Integer revisaledQuestionCount;

	// 订正答案批改状态true:已批改 false:未批改
	private Boolean revisalAnswerCorrectStatus;

	// 留言处理，支持多条留言
	private List<VHomeworkMessage> messages;

	/**
	 * 学生作业批改状态（教师视角）.
	 * 
	 * @since 小优快批
	 */
	private StudentHomeworkCorrectStatus correctStatus = StudentHomeworkCorrectStatus.DEFAULT;

	/**
	 * 人工批改中的题目数量
	 * 
	 * @since 小优快批
	 */
	private Integer correctingCount;

	/**
	 * 已经批改好了的题目数
	 * 
	 * @since 小优快批
	 */
	private Integer correctedCount;

	/**
	 * 待批改题目数
	 * @since 小优快批
	 */
	private Integer toBeCorrectedCount;
	
	/**
	 * 订正答案老师批改状态 false:需要老师批改
	 */
	private Boolean revisalAnswerTeacherCorrectStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public VHomeworkStudentClazz getStudentClazz() {
		return studentClazz;
	}

	public void setStudentClazz(VHomeworkStudentClazz studentClazz) {
		this.studentClazz = studentClazz;
	}

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getSubmitAt() {
		return submitAt;
	}

	public void setSubmitAt(Date submitAt) {
		this.submitAt = submitAt;
	}

	public Date getStuSubmitAt() {
		return stuSubmitAt;
	}

	public void setStuSubmitAt(Date stuSubmitAt) {
		this.stuSubmitAt = stuSubmitAt;
	}

	public Date getIssueAt() {
		return issueAt;
	}

	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}

	public StudentHomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(StudentHomeworkStatus status) {
		this.status = status;
	}

	public String getStatusName() {
		if (StringUtils.isBlank(statusName)) {
			String sn = "";
			if (getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
				sn = "未提交";
			} else if (getStatus() == StudentHomeworkStatus.SUBMITED) {
				sn = "已提交";
			} else if (getStatus() == StudentHomeworkStatus.ISSUED) {
				sn = "下发";
			}
			setStatusName(sn);
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Deprecated
	public boolean isCorrected() {
		return corrected;
	}

	@Deprecated
	public void setCorrected(boolean corrected) {
		this.corrected = corrected;
	}

	@Deprecated
	public boolean isStudentCorrected() {
		return studentCorrected;
	}

	@Deprecated
	public void setStudentCorrected(boolean studentCorrected) {
		this.studentCorrected = studentCorrected;
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
				setRightRateTitle(
						String.valueOf(getRightRate().setScale(2, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}
	
	public BigDecimal getRightRateCorrect() {
		return rightRateCorrect;
	}

	public void setRightRateCorrect(BigDecimal rightRateCorrect) {
		this.rightRateCorrect = rightRateCorrect;
	}

	public String getRightRateCorrectTitle() {
		if (StringUtils.isBlank(rightRateCorrectTitle)) {
			if (getRightRateCorrect() == null) {
				setRightRateCorrectTitle(StringUtils.EMPTY);
			} else {
				setRightRateCorrectTitle(
						String.valueOf(getRightRateCorrect().setScale(2, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateCorrectTitle;
	}

	public void setRightRateCorrectTitle(String rightRateCorrectTitle) {
		this.rightRateCorrectTitle = rightRateCorrectTitle;
	}

	public int getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(int homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
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

	@Deprecated
	public boolean isAutoManualAllCorrected() {
		return autoManualAllCorrected;
	}

	@Deprecated
	public void setAutoManualAllCorrected(boolean autoManualAllCorrected) {
		this.autoManualAllCorrected = autoManualAllCorrected;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public String getCompletionRateTitle() {
		if (StringUtils.isBlank(completionRateTitle)) {
			if (getCompletionRate() == null) {
				setCompletionRateTitle(StringUtils.EMPTY);
			} else {
				setCompletionRateTitle(
						String.valueOf(getCompletionRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return completionRateTitle;
	}

	public void setCompletionRateTitle(String completionRateTitle) {
		this.completionRateTitle = completionRateTitle;
	}

	public VHomework getHomework() {
		return homework;
	}

	public void setHomework(VHomework homework) {
		this.homework = homework;
	}

	public int getCompletionCount() {
		return completionCount;
	}

	public void setCompletionCount(int completionCount) {
		this.completionCount = completionCount;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public boolean isTimeout() {
		return timeout;
	}

	public void setTimeout(boolean timeout) {
		this.timeout = timeout;
	}

	public StudentHomeworkCorrectStatus getCorrectStatus() {
		return correctStatus;
	}

	public void setCorrectStatus(StudentHomeworkCorrectStatus correctStatus) {
		this.correctStatus = correctStatus;
	}

	public Integer getRevisalQuestionTotal() {
		return revisalQuestionTotal;
	}

	public void setRevisalQuestionTotal(Integer revisalQuestionTotal) {
		this.revisalQuestionTotal = revisalQuestionTotal;
	}

	public Integer getRevisaledQuestionCount() {
		return revisaledQuestionCount;
	}

	public void setRevisaledQuestionCount(Integer revisaledQuestionCount) {
		this.revisaledQuestionCount = revisaledQuestionCount;
	}

	public Boolean getRevisalAnswerCorrectStatus() {
		return revisalAnswerCorrectStatus;
	}

	public void setRevisalAnswerCorrectStatus(Boolean revisalAnswerCorrectStatus) {
		this.revisalAnswerCorrectStatus = revisalAnswerCorrectStatus;
	}

	public List<VHomeworkMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<VHomeworkMessage> messages) {
		this.messages = messages;
	}

	public Integer getCorrectingCount() {
		return correctingCount;
	}

	public void setCorrectingCount(Integer correctingCount) {
		this.correctingCount = correctingCount;
	}

	public Integer getCorrectedCount() {
		return correctedCount;
	}

	public void setCorrectedCount(Integer correctedCount) {
		this.correctedCount = correctedCount;
	}

	public Integer getToBeCorrectedCount() {
		return toBeCorrectedCount;
	}

	public void setToBeCorrectedCount(Integer toBeCorrectedCount) {
		this.toBeCorrectedCount = toBeCorrectedCount;
	}

	public Boolean getRevisalAnswerTeacherCorrectStatus() {
		return revisalAnswerTeacherCorrectStatus;
	}

	public void setRevisalAnswerTeacherCorrectStatus(Boolean revisalAnswerTeacherCorrectStatus) {
		this.revisalAnswerTeacherCorrectStatus = revisalAnswerTeacherCorrectStatus;
	}

}
