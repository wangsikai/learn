package com.lanking.cloud.domain.yoomath.holidayHomework;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;

/**
 * 学生假日作业项-题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "holiday_stu_homework_item_question")
public class HolidayStuHomeworkItemQuestion implements Serializable {

	private static final long serialVersionUID = 6314726321678588020L;

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
	 * 假日作业ID {@link HolidayHomework}.id
	 */
	@Column(name = "holiday_homework_id")
	private Long holidayHomeworkId;

	/**
	 * 假日作业项ID {@link HolidayHomeworkItem}.id
	 */
	@Column(name = "holiday_homework_item_id")
	private Long holidayHomeworkItemId;

	/**
	 * 学生假日作业ID {@link HolidayStuHomework}.id
	 */
	@Column(name = "holiday_stu_homework_id")
	private Long holidayStuHomeworkId;

	/**
	 * 学生假日作业项ID {@link HolidayStuHomeworkItem}.id
	 */
	@Column(name = "holiday_stu_homework_item_id")
	private Long holidayStuHomeworkItemId;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 是否是子题
	 */
	@Column(name = "sub_flag")
	private Boolean subFlag;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 自动批改结果
	 */
	@Column(name = "auto_result", precision = 3)
	private HomeworkAnswerResult autoResult;

	/**
	 * 自动批改时间
	 */
	@Column(name = "auto_correct_at", columnDefinition = "datetime(3)")
	private Date autoCorrectAt;

	/**
	 * 批改评语
	 */
	@Column(name = "comment", length = 500)
	private String comment;

	/**
	 * 结果
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;

	/**
	 * 批改时间
	 */
	@Column(name = "correct_at", columnDefinition = "datetime(3)")
	private Date correctAt;

	/**
	 * 机器有没有批改过
	 */
	@Column(name = "auto_correct")
	private boolean autoCorrect = false;

	/**
	 * 人工有没有批改过
	 */
	@Column(name = "manual_correct")
	private boolean manualCorrect = false;

	/**
	 * 答案图片
	 */
	@Column(name = "answer_img")
	private Long answerImg;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", precision = 5)
	private Integer rightRate;

	/**
	 * 确认状态
	 */
	@Column(name = "confirm_status", precision = 3)
	private HomeworkConfirmStatus confirmStatus;

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

	public Long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(Long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

	public Long getHolidayHomeworkItemId() {
		return holidayHomeworkItemId;
	}

	public void setHolidayHomeworkItemId(Long holidayHomeworkItemId) {
		this.holidayHomeworkItemId = holidayHomeworkItemId;
	}

	public Long getHolidayStuHomeworkId() {
		return holidayStuHomeworkId;
	}

	public void setHolidayStuHomeworkId(Long holidayStuHomeworkId) {
		this.holidayStuHomeworkId = holidayStuHomeworkId;
	}

	public Long getHolidayStuHomeworkItemId() {
		return holidayStuHomeworkItemId;
	}

	public void setHolidayStuHomeworkItemId(Long holidayStuHomeworkItemId) {
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Boolean getSubFlag() {
		return subFlag;
	}

	public void setSubFlag(Boolean subFlag) {
		this.subFlag = subFlag;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public HomeworkAnswerResult getAutoResult() {
		return autoResult;
	}

	public void setAutoResult(HomeworkAnswerResult autoResult) {
		this.autoResult = autoResult;
	}

	public Date getAutoCorrectAt() {
		return autoCorrectAt;
	}

	public void setAutoCorrectAt(Date autoCorrectAt) {
		this.autoCorrectAt = autoCorrectAt;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public Date getCorrectAt() {
		return correctAt;
	}

	public void setCorrectAt(Date correctAt) {
		this.correctAt = correctAt;
	}

	public boolean isAutoCorrect() {
		return autoCorrect;
	}

	public void setAutoCorrect(boolean autoCorrect) {
		this.autoCorrect = autoCorrect;
	}

	public boolean isManualCorrect() {
		return manualCorrect;
	}

	public void setManualCorrect(boolean manualCorrect) {
		this.manualCorrect = manualCorrect;
	}

	public Long getAnswerImg() {
		return answerImg;
	}

	public void setAnswerImg(Long answerImg) {
		this.answerImg = answerImg;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public HomeworkConfirmStatus getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(HomeworkConfirmStatus confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

}
