package com.lanking.cloud.domain.base.intelligentCorrection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 答案归档
 * 
 * <pre>
 * 1.正确答案归档到此表中
 * 2.错误答案按照一定规则进入此归档表
 * 归档数据用作智能批改使用
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "answer_archive")
public class AnswerArchive implements Serializable {

	private static final long serialVersionUID = 8280587720184150500L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 标准答案ID
	 */
	@Column(name = "answer_id")
	private Long answerId;

	/**
	 * 归档答案
	 */
	@Column(name = "content", length = 1024)
	private String content;

	/**
	 * 答案批改结果
	 * 
	 * @see HomeworkAnswerResult
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public String getContent() {
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

}
