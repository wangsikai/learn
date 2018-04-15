package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 知识点回顾包含的知识点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_knoledgespec_kp")
public class TeachAssistElementKnowledgeSpecKp implements Serializable {
	private static final long serialVersionUID = 6741335767000208140L;
	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属知识点回顾模块ID
	 */
	@Column(name = "knowledge_spec_id")
	private Long knowledgeSpecId;

	/**
	 * 知识点代码
	 */
	@Column(name = "knowledge_code")
	private Long knowledgeCode;

	/**
	 * 知识点内容
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content")
	private String content;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getKnowledgeSpecId() {
		return knowledgeSpecId;
	}

	public void setKnowledgeSpecId(Long knowledgeSpecId) {
		this.knowledgeSpecId = knowledgeSpecId;
	}

	public Long getKnowledgeCode() {
		return knowledgeCode;
	}

	public void setKnowledgeCode(Long knowledgeCode) {
		this.knowledgeCode = knowledgeCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
