package com.lanking.cloud.domain.common.resource.examPaper;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 试卷题型分类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "exam_paper_topic")
public class ExamPaperTopic implements Serializable {
	private static final long serialVersionUID = 7807615672659901130L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 试卷ID
	 */
	@Column(name = "exam_paper_id")
	private Long examPaperId;

	/**
	 * 分类名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 类型
	 * 
	 * @see ExamPaperTopicType
	 */
	@Column(name = "type")
	private ExamPaperTopicType type;

	/**
	 * 序号
	 */
	@Column(name = "sequence")
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(Long examPaperId) {
		this.examPaperId = examPaperId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExamPaperTopicType getType() {
		return type;
	}

	public void setType(ExamPaperTopicType type) {
		this.type = type;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
