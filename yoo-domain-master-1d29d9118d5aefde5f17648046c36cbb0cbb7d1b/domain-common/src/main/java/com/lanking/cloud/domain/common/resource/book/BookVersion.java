package com.lanking.cloud.domain.common.resource.book;

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
import org.hibernate.annotations.TypeDef;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.LongTypeList;

/**
 * 书本版本
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "book_version")
@TypeDef(name = "list", defaultForType = List.class, typeClass = LongTypeList.class)
public class BookVersion implements Serializable {

	private static final long serialVersionUID = 1145780703006194790L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 版本号
	 */
	@Column(name = "version")
	private Integer version = 1;

	/**
	 * 所属书本ID
	 */
	@Column(name = "book_id")
	private Long bookId;

	/**
	 * 封面
	 */
	@Column(name = "cover_id")
	private Long coverId;

	/**
	 * 主版本标记，为真时表示该版本为当前主版本
	 */
	@Column(name = "main_flag")
	private boolean mainFlag = true;

	/**
	 * 书本名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 书本简称
	 */
	@Column(name = "short_name", length = 40)
	private String shortName;

	/**
	 * 书本类型
	 */
	@Column(name = "book_category", precision = 3)
	private BookCategory bookCategory;

	/**
	 * 书本类型
	 */
	@Column(name = "resource_category_code")
	private Integer resourceCategoryCode;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 教材版本代码
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 直接对应的章节
	 */
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 顺序章节
	 */
	@Type(type = "list")
	@Column(name = "section_codes", length = 100)
	private List<Long> sectionCodes;

	/**
	 * ISBN号
	 */
	@Column(name = "isbn", length = 20)
	private String isbn;

	/**
	 * 出版社
	 */
	@Column(name = "press", length = 50)
	private String press;

	/**
	 * 备注
	 */
	@Column(name = "description", length = 500)
	private String description;

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
	 * 版本状态
	 */
	@Column(precision = 3, nullable = false)
	private BookStatus status = BookStatus.EDITING;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public boolean isMainFlag() {
		return mainFlag;
	}

	public void setMainFlag(boolean mainFlag) {
		this.mainFlag = mainFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public BookCategory getBookCategory() {
		return bookCategory;
	}

	public void setBookCategory(BookCategory bookCategory) {
		this.bookCategory = bookCategory;
	}

	public Integer getResourceCategoryCode() {
		return resourceCategoryCode;
	}

	public void setResourceCategoryCode(Integer resourceCategoryCode) {
		this.resourceCategoryCode = resourceCategoryCode;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

}
