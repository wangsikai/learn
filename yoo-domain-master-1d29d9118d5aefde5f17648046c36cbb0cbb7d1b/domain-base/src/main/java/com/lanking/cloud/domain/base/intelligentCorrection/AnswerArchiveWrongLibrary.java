package com.lanking.cloud.domain.base.intelligentCorrection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 错误答案归档
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "answer_archive_wrong_library")
public class AnswerArchiveWrongLibrary implements Serializable {

	private static final long serialVersionUID = 8985517624776826578L;

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
	 * 错误答案内容
	 */
	@Column(name = "content", length = 1024)
	private String content;

	/**
	 * 错误答案出现次数
	 */
	@Column(name = "times")
	private long times;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status;

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

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
