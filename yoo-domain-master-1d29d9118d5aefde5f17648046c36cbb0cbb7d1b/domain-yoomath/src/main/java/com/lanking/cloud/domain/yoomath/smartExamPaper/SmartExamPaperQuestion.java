package com.lanking.cloud.domain.yoomath.smartExamPaper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 智能试卷题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "smart_exam_paper_question")
public class SmartExamPaperQuestion implements Serializable {

	private static final long serialVersionUID = -2870889239758396886L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 智能出卷ID
	 */
	@Column(name = "paper_id")
	private long paperId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private long questionId;

	/**
	 * 答案
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "answer", length = 4000)
	private Map<Long, List<String>> answer;

	/**
	 * 答案结果
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;

	/**
	 * 是否答过题
	 */
	@Column(name = "done", columnDefinition = "bit default 0")
	private boolean done = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getPaperId() {
		return paperId;
	}

	public void setPaperId(long paperId) {
		this.paperId = paperId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public Map<Long, List<String>> getAnswer() {
		return answer;
	}

	public void setAnswer(Map<Long, List<String>> answer) {
		this.answer = answer;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

}
