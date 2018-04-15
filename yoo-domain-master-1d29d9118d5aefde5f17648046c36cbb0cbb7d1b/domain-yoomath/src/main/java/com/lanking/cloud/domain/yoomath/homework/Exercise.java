package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 练习
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "exercise")
public class Exercise implements Serializable {

	private static final long serialVersionUID = 3668946315003219644L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 年级代码
	 */
	@Deprecated
	@Column(name = "grade_code")
	private Integer gradeCode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 预置习题ID {@link TextbookExercise}.id
	 */
	@Column(name = "textbook_exercise_id")
	private Long textbookExerciseId;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 教材版本代码
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 教材代码
	 */
	@Column(name = "text_code")
	private Integer textCode;

	/**
	 * 章节代码
	 */
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 书本的ID {@link Book}.id
	 */
	@Column(name = "book_id")
	private Long bookId;

	/**
	 * TODO ???
	 */
	@Deprecated
	@Column(name = "category", precision = 7)
	private Integer resourceCategory;

	/**
	 * 状态
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(Integer gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTextbookExerciseId() {
		return textbookExerciseId;
	}

	public void setTextbookExerciseId(Long textbookExerciseId) {
		this.textbookExerciseId = textbookExerciseId;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getTextCode() {
		return textCode;
	}

	public void setTextCode(Integer textCode) {
		this.textCode = textCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Integer getResourceCategory() {
		if (resourceCategory == null) {
			return 0;
		}
		return resourceCategory;
	}

	public void setResourceCategory(Integer resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
