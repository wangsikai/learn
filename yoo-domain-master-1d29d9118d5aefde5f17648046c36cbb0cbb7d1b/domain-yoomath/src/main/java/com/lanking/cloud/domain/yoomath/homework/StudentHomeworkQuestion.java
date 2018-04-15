package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;

/**
 * 学生作业题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_homework_question")
public class StudentHomeworkQuestion implements Serializable {

	private static final long serialVersionUID = -4016218790852199962L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生作业ID {@link StudentHomework}.id
	 */
	@Column(name = "student_homework_id")
	private Long studentHomeworkId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 批改结果
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;

	/**
	 * 批注
	 */
	@Column(name = "comment", length = 500)
	private String comment;

	/**
	 * 批改时间
	 */
	@Column(name = "correct_at", columnDefinition = "datetime(3)")
	private Date correctAt;

	/**
	 * 是否是子题(浩哥的数据加工不要使用此字段)
	 */
	@Column(name = "sub_flag")
	private Boolean subFlag;

	/**
	 * 是否是订正题(默认值为false)
	 */
	@Column(name = "correct", columnDefinition = "bit default 0")
	private boolean correct = false;

	/**
	 * 机器有没有批改过
	 */
	@Column(name = "auto_correct", columnDefinition = "bit default 0")
	private boolean autoCorrect = false;

	/**
	 * 有没有手动批改过
	 * 
	 * @since 小悠快批，2018-2-13，新流程下该字段表示是否经过手动批改（包括小悠快批、教师自行批改、管理员批改等），切记！
	 */
	@Column(name = "manual_correct", columnDefinition = "bit default 0")
	private boolean manualCorrect = false;

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
	 * 答案图片标记内容
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "answer_notation")
	private String answerNotation;

	/**
	 * 手机端批注的点
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "answer_notation_points")
	private String answerNotationPoints;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", precision = 5)
	private Integer rightRate;

	/**
	 * 题目类型
	 */
	@Column(name = "type", precision = 3)
	private Type type;

	/**
	 * 确认状态.
	 * 
	 */
	@Column(name = "confirm_status", precision = 3)
	private HomeworkConfirmStatus confirmStatus;

	/**
	 * 做题计时（秒数）.
	 */
	@Column(name = "do_time", columnDefinition = "bigint default 0")
	private Integer dotime;

	/**
	 * 老师语音时间,为0时表示没有语音
	 */
	@Column(name = "voice_time")
	private Integer voiceTime;

	/**
	 * 文件存储hash key值,关联于七牛存储
	 */
	@Column(name = "voice_file_key")
	private String voiceFileKey;

	/**
	 * 习题的预期批改方式（可能还未批改）.
	 * 
	 * @since 小优快批
	 */
	@Column(name = "correct_type")
	private QuestionCorrectType correctType = QuestionCorrectType.DEFAULT;

	/**
	 * 习题的最终批改方式.
	 * 
	 * @since 小优快批
	 */
	@Column(name = "correct_final_type")
	private QuestionCorrectType correctFinalType = QuestionCorrectType.DEFAULT;

	/**
	 * 是否已订正（订正题时可用）.
	 * 
	 * @since 小优快批
	 */
	@Column(name = "is_revised", columnDefinition = "bit default 0")
	private Boolean isRevised = false;

	/**
	 * 是否是新订正题(默认值为false，小悠快批上线后新流程下的订正题才有此标记)
	 * 
	 * @since 小优快批
	 */
	@Column(name = "new_correct", columnDefinition = "bit default 0")
	private boolean newCorrect = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(Long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCorrectAt() {
		return correctAt;
	}

	public void setCorrectAt(Date correctAt) {
		this.correctAt = correctAt;
	}

	public Boolean getSubFlag() {
		return subFlag;
	}

	public void setSubFlag(Boolean subFlag) {
		this.subFlag = subFlag;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
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

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @since
	 * @return
	 */
	public HomeworkConfirmStatus getConfirmStatus() {
		return confirmStatus;
	}

	/**
	 * @since
	 * @param confirmStatus
	 */
	public void setConfirmStatus(HomeworkConfirmStatus confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Integer getDotime() {
		return dotime;
	}

	public void setDotime(Integer dotime) {
		this.dotime = dotime;
	}

	public Integer getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(Integer voiceTime) {
		this.voiceTime = voiceTime;
	}

	public String getVoiceFileKey() {
		return voiceFileKey;
	}

	public void setVoiceFileKey(String voiceFileKey) {
		this.voiceFileKey = voiceFileKey;
	}

	public QuestionCorrectType getCorrectType() {
		return correctType;
	}

	public void setCorrectType(QuestionCorrectType correctType) {
		this.correctType = correctType;
	}

	public boolean isRevised() {
		return isRevised;
	}

	public void setRevised(boolean isRevised) {
		this.isRevised = isRevised;
	}

	public Boolean getIsRevised() {
		return isRevised;
	}

	public void setIsRevised(Boolean isRevised) {
		this.isRevised = isRevised;
	}

	public boolean isNewCorrect() {
		return newCorrect;
	}

	public void setNewCorrect(boolean newCorrect) {
		this.newCorrect = newCorrect;
	}

	public QuestionCorrectType getCorrectFinalType() {
		return correctFinalType;
	}

	public void setCorrectFinalType(QuestionCorrectType correctFinalType) {
		this.correctFinalType = correctFinalType;
	}
}
