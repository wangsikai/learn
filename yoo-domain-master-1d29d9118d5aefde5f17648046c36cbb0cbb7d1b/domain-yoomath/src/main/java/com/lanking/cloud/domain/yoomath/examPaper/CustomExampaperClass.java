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
 * 组卷&班级关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "custom_exampaper_class")
public class CustomExampaperClass implements Serializable {

	private static final long serialVersionUID = -557194915310232435L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 组卷ID {@link CustomExampaper}.id
	 */
	@Column(name = "custom_exampaper_id")
	private long customExampaperId;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private long classId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCustomExampaperId() {
		return customExampaperId;
	}

	public void setCustomExampaperId(long customExampaperId) {
		this.customExampaperId = customExampaperId;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

}
