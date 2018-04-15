package com.lanking.uxb.rescon.book.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * 书本版本VO.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月23日
 */
public class VBookVersion implements Serializable {
	private static final long serialVersionUID = -445144498228441098L;

	private Long id;

	private Long bookId;

	/**
	 * 主版本标记.
	 */
	private boolean mainFlag;

	/**
	 * 封面.
	 */
	private Long coverId;

	private String coverUrl;

	/**
	 * 书本名称.
	 */
	private String name;

	/**
	 * 书本简称.
	 */
	private String shortName;

	/**
	 * 阶段.
	 */
	private VPhase phase;

	/**
	 * 科目.
	 */
	private VSubject subject;

	/**
	 * 书本类型.
	 */
	private VResourceCategory resourceCategory;

	/**
	 * 教材版本.
	 */
	private VTextbookCategory textbookCategory;

	/**
	 * 教材.
	 */
	private VTextbook textbook;

	/**
	 * 顺序章节.
	 */
	private List<VSection> sections = Lists.newArrayList();

	/**
	 * ISBN.
	 */
	private String isbn;

	/**
	 * 版本.
	 */
	private Integer version;

	/**
	 * 版本状态.
	 */
	private BookStatus status;

	/**
	 * 版本描述.
	 */
	private String description;

	/**
	 * 出版社.
	 */
	private String press;

	/**
	 * 创建人.
	 */
	private VVendorUser creator;

	/**
	 * 创建时间.
	 */
	private Date createAt;

	/**
	 * 未转换v3知识点的题目个数.
	 */
	private int noV3Count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public VSubject getSubject() {
		return subject;
	}

	public void setSubject(VSubject subject) {
		this.subject = subject;
	}

	public VTextbookCategory getTextbookCategory() {
		return textbookCategory;
	}

	public void setTextbookCategory(VTextbookCategory textbookCategory) {
		this.textbookCategory = textbookCategory;
	}

	public VTextbook getTextbook() {
		return textbook;
	}

	public void setTextbook(VTextbook textbook) {
		this.textbook = textbook;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public VVendorUser getCreator() {
		return creator;
	}

	public void setCreator(VVendorUser creator) {
		this.creator = creator;
	}

	public List<VSection> getSections() {
		return sections;
	}

	public void setSections(List<VSection> sections) {
		this.sections = sections;
	}

	public boolean isMainFlag() {
		return mainFlag;
	}

	public void setMainFlag(boolean mainFlag) {
		this.mainFlag = mainFlag;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public VResourceCategory getResourceCategory() {
		return resourceCategory;
	}

	public void setResourceCategory(VResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getNoV3Count() {
		return noV3Count;
	}

	public void setNoV3Count(int noV3Count) {
		this.noV3Count = noV3Count;
	}

}
