package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 学生作业答案
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_homework_answer")
public class StudentHomeworkAnswer implements Serializable {

	private static final long serialVersionUID = -3484036507883249412L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生作业题目ID {@link StudentHomeworkQuestion}.id
	 */
	@Column(name = "student_homework_question_id")
	private Long studentHomeworkQuestionId;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	/**
	 * 答案内容
	 */
	@Column(name = "content", length = 1000)
	private String content;

	/**
	 * 答案ascii内容
	 */
	@Column(name = "content_ascii", length = 1000)
	private String contentAscii;

	/**
	 * 答案结果
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;

	/**
	 * 自动批改结果
	 */
	@Column(name = "auto_result", precision = 3)
	private HomeworkAnswerResult autoResult;

	/**
	 * 批改结果是否可信
	 * 
	 * @since 小优快批，不再使用该属性 2018-2-8
	 */
	@Deprecated
	@Column(name = "credible")
	private Boolean credible;

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
	 * 批改时间
	 */
	@Column(name = "correct_at", columnDefinition = "datetime(3)")
	private Date correctAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentHomeworkQuestionId() {
		return studentHomeworkQuestionId;
	}

	public void setStudentHomeworkQuestionId(Long studentHomeworkQuestionId) {
		this.studentHomeworkQuestionId = studentHomeworkQuestionId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getContent() {
		// 处理丢失标签的问题
		if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(contentAscii)) {
			Pattern p = Pattern.compile("<ux-mth>(.+?)</ux-mth>");
			Matcher m = p.matcher(contentAscii);
			if (m.find() && content.indexOf("<ux-mth>") == -1) {
				content = "<ux-mth>" + content + "</ux-mth>";
			}
		}
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public HomeworkAnswerResult getAutoResult() {
		return autoResult;
	}

	public void setAutoResult(HomeworkAnswerResult autoResult) {
		this.autoResult = autoResult;
	}

	/**
	 * 
	 * @since 小优快批，不再使用该属性 2018-2-8
	 */
	@Deprecated
	public Boolean getCredible() {
		return credible;
	}

	/**
	 * 
	 * @since 小优快批，不再使用该属性 2018-2-8
	 */
	@Deprecated
	public void setCredible(Boolean credible) {
		this.credible = credible;
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

	public Date getCorrectAt() {
		return correctAt;
	}

	public void setCorrectAt(Date correctAt) {
		this.correctAt = correctAt;
	}

	public String getContentAscii() {
		return contentAscii;
	}

	public void setContentAscii(String contentAscii) {
		this.contentAscii = contentAscii;
	}

}
