package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 考点-知识点对应关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "examination_point_knowledge_point")
public class ExaminationPointKnowledgePoint implements Serializable {

	private static final long serialVersionUID = -7523622362274995100L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 考点ID
	 */
	@Column(name = "examination_point_id")
	private long examinationPointId;

	/**
	 * 知识点CODE
	 */
	@Column(name = "knowledge_point_code")
	private long knowledgePointCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getExaminationPointId() {
		return examinationPointId;
	}

	public void setExaminationPointId(long examinationPointId) {
		this.examinationPointId = examinationPointId;
	}

	public long getKnowledgePointCode() {
		return knowledgePointCode;
	}

	public void setKnowledgePointCode(long knowledgePointCode) {
		this.knowledgePointCode = knowledgePointCode;
	}

}
