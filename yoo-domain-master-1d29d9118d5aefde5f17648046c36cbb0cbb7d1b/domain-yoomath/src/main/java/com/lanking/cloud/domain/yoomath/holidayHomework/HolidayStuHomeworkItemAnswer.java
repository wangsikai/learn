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

/**
 * 学生假日作业项-答案
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "holiday_stu_homework_item_answer")
public class HolidayStuHomeworkItemAnswer implements Serializable {

	private static final long serialVersionUID = -4314134174248885220L;

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
	 * 学生假日作业项-题目ID {@link HolidayStuHomeworkItemQuestion}.id
	 */
	@Column(name = "holiday_stu_homework_item_qid")
	private Long holidayStuHomeworkItemQuestionId;

	/**
	 * 顺序
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	/**
	 * latex答案内容(如果是多选题直接拼接一个字符串放入，如：ABD)
	 */
	@Column(name = "content", length = 1000)
	private String content;

	/**
	 * ascii math 答案
	 */
	@Column(name = "content_ascii", length = 1000)
	private String contentAscii;

	/**
	 * 答题人ID
	 */
	@Column(name = "answer_id")
	private Long answerId;

	/**
	 * 答题时间
	 */
	@Column(name = "answer_at", columnDefinition = "datetime(3)")
	private Date answerAt;

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
	 * 自动批改结果是否可信
	 */
	@Column(name = "credible")
	private Boolean credible;

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

	public Long getHolidayStuHomeworkItemQuestionId() {
		return holidayStuHomeworkItemQuestionId;
	}

	public void setHolidayStuHomeworkItemQuestionId(Long holidayStuHomeworkItemQuestionId) {
		this.holidayStuHomeworkItemQuestionId = holidayStuHomeworkItemQuestionId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentAscii() {
		return contentAscii;
	}

	public void setContentAscii(String contentAscii) {
		this.contentAscii = contentAscii;
	}

	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public Date getAnswerAt() {
		return answerAt;
	}

	public void setAnswerAt(Date answerAt) {
		this.answerAt = answerAt;
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

	public Boolean getCredible() {
		return credible;
	}

	public void setCredible(Boolean credible) {
		this.credible = credible;
	}

}
