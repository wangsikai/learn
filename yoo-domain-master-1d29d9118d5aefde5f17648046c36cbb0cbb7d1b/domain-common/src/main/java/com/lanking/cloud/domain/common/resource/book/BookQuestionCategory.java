package com.lanking.cloud.domain.common.resource.book;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * 书本题目类目
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年10月26日
 */
@Getter
@Setter
@Entity
@Table(name = "book_question_category")
public class BookQuestionCategory implements Serializable {

	private static final long serialVersionUID = -4766653287330412496L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 书本ID
	 */
	@Column(name = "book_version_id")
	private Long bookVersionId;

	/**
	 * 书本章节ID
	 */
	@Column(name = "book_section_id")
	private Long bookSectionId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 64)
	private String name;

}
