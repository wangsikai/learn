package com.lanking.cloud.domain.yoomath.fallible;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;

/**
 * ocr以及search结果记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "fallible_ocr_search_record")
public class FallibleOcrSearchRecord implements Serializable {

	private static final long serialVersionUID = -3646047106335713572L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * ocr对应的图片
	 */
	@Column(name = "file_id")
	private long fileId;

	/**
	 * 搜索匹配出来的题目ID列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "questions")
	private List<Long> questions = Lists.newArrayList();

	/**
	 * 用户选择的题目ID
	 */
	@Column(name = "choose_question")
	private Long chooseQuestion;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 确认选择题目时间
	 */
	@Column(name = "choose_at", columnDefinition = "datetime(3)")
	private Date chooseAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public List<Long> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Long> questions) {
		this.questions = questions;
	}

	public Long getChooseQuestion() {
		return chooseQuestion;
	}

	public void setChooseQuestion(Long chooseQuestion) {
		this.chooseQuestion = chooseQuestion;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getChooseAt() {
		return chooseAt;
	}

	public void setChooseAt(Date chooseAt) {
		this.chooseAt = chooseAt;
	}

}