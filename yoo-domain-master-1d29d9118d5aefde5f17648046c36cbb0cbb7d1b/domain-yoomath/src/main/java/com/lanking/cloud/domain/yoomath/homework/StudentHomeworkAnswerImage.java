package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 学生作业答案图片,类型:
 * 
 * <pre>
 * 1.学生作业题目对应的答案图片(StudentHomeworkQuestion),此时student_homework_answer_id=0,student_homework_question_id为对应的ID
 * 
 * 注意：
 * answer_img、notation_answer_img、notation_web_img、notation_mobile_img、answer_notation、answer_notation_points
 * 这六个字段的存储逻辑参照接口ZyStudentHomeworkQuestionServiceImpl.saveNotation
 * 返回逻辑参照StudentHomeworkQuestionConvert
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_homework_answer_image")
public class StudentHomeworkAnswerImage implements Serializable {

	private static final long serialVersionUID = -5411524061504757130L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生作业题目ID {@link StudentHomeworkQuestion}.id
	 */
	@Column(name = "student_homework_question_id")
	private long studentHomeworkQuestionId = 0;

	/**
	 * 答案图片(用户上传的原始图片)
	 */
	@Column(name = "answer_img")
	private Long answerImg;

	/**
	 * 每次批注合成的图片
	 */
	@Column(name = "notation_answer_img")
	private Long notationAnswerImg;

	/**
	 * 每次批注的原图(web)
	 */
	@Column(name = "notation_web_img")
	private Long notationWebImg;

	/**
	 * 每次批注的原图(mobile)
	 */
	@Column(name = "notation_mobile_img")
	private Long notationMobileImg;

	/**
	 * web端批注内容
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "answer_notation")
	private String answerNotation;

	/**
	 * 手机端批注内容
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "answer_notation_points")
	private String answerNotationPoints;

	/**
	 * 手写轨迹(目前存储墨水屏手写轨迹)
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "hand_writing")
	private String handWriting;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getStudentHomeworkQuestionId() {
		return studentHomeworkQuestionId;
	}

	public void setStudentHomeworkQuestionId(long studentHomeworkQuestionId) {
		this.studentHomeworkQuestionId = studentHomeworkQuestionId;
	}

	public Long getAnswerImg() {
		return answerImg;
	}

	public void setAnswerImg(Long answerImg) {
		this.answerImg = answerImg;
	}

	public Long getNotationAnswerImg() {
		return notationAnswerImg;
	}

	public void setNotationAnswerImg(Long notationAnswerImg) {
		this.notationAnswerImg = notationAnswerImg;
	}

	public Long getNotationWebImg() {
		return notationWebImg;
	}

	public void setNotationWebImg(Long notationWebImg) {
		this.notationWebImg = notationWebImg;
	}

	public Long getNotationMobileImg() {
		return notationMobileImg;
	}

	public void setNotationMobileImg(Long notationMobileImg) {
		this.notationMobileImg = notationMobileImg;
	}

	public String getAnswerNotation() {
		return answerNotation;
	}

	public void setAnswerNotation(String answerNotation) {
		this.answerNotation = answerNotation;
	}

	public String getAnswerNotationPoints() {
		return answerNotationPoints;
	}

	public void setAnswerNotationPoints(String answerNotationPoints) {
		this.answerNotationPoints = answerNotationPoints;
	}

	public String getHandWriting() {
		return handWriting;
	}

	public void setHandWriting(String handWriting) {
		this.handWriting = handWriting;
	}

}
