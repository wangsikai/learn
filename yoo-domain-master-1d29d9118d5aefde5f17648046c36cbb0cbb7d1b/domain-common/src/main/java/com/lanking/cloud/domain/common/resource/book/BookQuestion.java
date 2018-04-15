package com.lanking.cloud.domain.common.resource.book;

import java.io.Serializable;
import java.util.Date;

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
 * 书本题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Getter
@Setter
@Entity
@Table(name = "book_question")
public class BookQuestion implements Serializable {
	private static final long serialVersionUID = 2509435499113703968L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 书本版本ID
	 */
	@Column(name = "book_version_id")
	private Long bookVersionId;

	/**
	 * 书本目录ID
	 */
	@Column(name = "book_catalog_id")
	private Long bookCatalogId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 题目顺序
	 */
	@Column(name = "sequence")
	private int sequence;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 题目分类(默认为0)
	 */
	@Column(name = "book_question_catalog_id")
	private Long bookQuestionCatalogId = 0L;

}
