package com.lanking.cloud.domain.yoomath.examPaper;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 一份组卷所对应的题目大的类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "custom_exampaper_topic")
public class CustomExampaperTopic implements Serializable {

	private static final long serialVersionUID = -6705792449956186194L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 组卷ID {@link CustomExampaper}.id
	 */
	@Column(name = "custom_exampaper_id")
	private Long customExampaperId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 序号
	 */
	@Column(name = "sequence")
	private Integer sequence;

	/**
	 * 类型
	 */
	@Column(name = "type", precision = 3)
	private CustomExampaperTopicType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomExampaperId() {
		return customExampaperId;
	}

	public void setCustomExampaperId(Long customExampaperId) {
		this.customExampaperId = customExampaperId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public CustomExampaperTopicType getType() {
		return type;
	}

	public void setType(CustomExampaperTopicType type) {
		this.type = type;
	}

}
