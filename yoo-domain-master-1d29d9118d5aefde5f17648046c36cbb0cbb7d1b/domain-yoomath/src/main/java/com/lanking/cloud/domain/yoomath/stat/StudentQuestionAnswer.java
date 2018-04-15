package com.lanking.cloud.domain.yoomath.stat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.sdk.util.CollectionUtils;

/**
 * 答题记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_question_answer")
public class StudentQuestionAnswer implements Serializable {

	private static final long serialVersionUID = 7445951692339001847L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private long questionId;

	/**
	 * 答案，存储latex JSON数组
	 */
	@Column(name = "answer", length = 4000)
	private String answer;

	/**
	 * 答案，存储asciimath JSON数组
	 */
	@Column(name = "answer_ascii", length = 4000)
	private String answerAscii;

	/**
	 * 答案图片列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "answer_imgs")
	private List<Long> answerImgs = Lists.newArrayList();

	/**
	 * 对错
	 */
	@Column(name = "result", columnDefinition = "tinyint default 3")
	private HomeworkAnswerResult result = HomeworkAnswerResult.UNKNOW;

	/**
	 * 项的对错
	 * 
	 * <pre>
	 * 1.填空题每空的对错情况
	 * </pre>
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "item_results")
	private List<HomeworkAnswerResult> itemResults = Lists.newArrayList();

	/**
	 * 简答题正确率
	 */
	@Column(name = "right_rate", precision = 5)
	private Integer rightRate;

	/**
	 * 来源(老数据的结果都设置成HOMEWORK)
	 */
	@Column(name = "source", columnDefinition = "tinyint default 0")
	private StudentQuestionAnswerSource source = StudentQuestionAnswerSource.HOMEWORK;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 解答题答案图片,遗留字段
	 */
	@Transient
	private Long answerImg;
	@Transient
	private Map<Long, List<String>> answers;
	@Transient
	private Map<Long, List<String>> answersAscii;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerAscii() {
		return answerAscii;
	}

	public void setAnswerAscii(String answerAscii) {
		this.answerAscii = answerAscii;
	}

	public void setAnswerImgs(List<Long> answerImgs) {
		this.answerImgs = answerImgs;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public List<HomeworkAnswerResult> getItemResults() {
		return itemResults;
	}

	public void setItemResults(List<HomeworkAnswerResult> itemResults) {
		this.itemResults = itemResults;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public StudentQuestionAnswerSource getSource() {
		return source;
	}

	public void setSource(StudentQuestionAnswerSource source) {
		this.source = source;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getAnswerImg() {
		if (CollectionUtils.isNotEmpty(getAnswerImgs())) {
			answerImg = getAnswerImgs().get(0);
		}
		return answerImg;
	}

	public List<Long> getAnswerImgs() {
		return answerImgs;
	}

	public Map<Long, List<String>> getAnswers() {
		return JSON.parseObject(getAnswer(), new TypeReference<Map<Long, List<String>>>() {
		});
	}

	public void setAnswers(Map<Long, List<String>> answers) {
		if (answers != null) {
			setAnswer(JSON.toJSONString(answers));
		}
		this.answers = answers;
	}

	public Map<Long, List<String>> getAnswersAscii() {
		return JSON.parseObject(getAnswerAscii(), new TypeReference<Map<Long, List<String>>>() {
		});
	}

	public void setAnswersAscii(Map<Long, List<String>> answersAscii) {
		if (answersAscii != null) {
			setAnswerAscii(JSON.toJSONString(answersAscii));
		}
		this.answersAscii = answersAscii;
	}

}
