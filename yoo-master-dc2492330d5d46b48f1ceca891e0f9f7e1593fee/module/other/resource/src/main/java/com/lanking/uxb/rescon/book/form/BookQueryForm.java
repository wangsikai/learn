package com.lanking.uxb.rescon.book.form;

import java.io.Serializable;

import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.cloud.sdk.bean.Valueable;
import com.lanking.cloud.sdk.data.Order.Direction;

/**
 * 书本查询表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月23日
 */
public class BookQueryForm implements Serializable {
	private static final long serialVersionUID = 542996357219273397L;

	/**
	 * 查询关键字.
	 */
	private String key;

	/**
	 * 创建人.
	 */
	private Long createId;

	/**
	 * 书本状态.
	 */
	private BookStatus status;

	/**
	 * 创建时间范围.
	 */
	private Long createBt;
	private Long createEt;

	/**
	 * 阶段.
	 */
	private Integer phaseCode;

	/**
	 * 学科.
	 */
	private Integer subjectCode;

	/**
	 * 教材版本.
	 */
	private Integer textbookCategoryCode;

	/**
	 * 教材.
	 */
	private Integer textbookCode;

	/**
	 * 章节.
	 */
	private Long sectionCode;

	/**
	 * 资源类型.
	 */
	private Integer resourceCategoryCode;

	/**
	 * 书本编码.
	 */
	private String bookCode;

	private Integer pageSize = 20;
	private Integer page = 1;

	/**
	 * 书本名称.
	 * 
	 * @since rescon UE v1.2.6
	 */
	private String name;

	/**
	 * 发布时间范围，开始时间.
	 * 
	 * @since rescon UE v1.2.6
	 */
	private Long publishBt;

	/**
	 * 发布时间范围，结束时间.
	 * 
	 * @since rescon UE v1.2.6
	 */
	private Long publishEt;

	/**
	 * 排序字段.
	 */
	private OrderType orderType;

	/**
	 * 排序方向.
	 */
	private Direction direction;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public Long getCreateBt() {
		return createBt;
	}

	public void setCreateBt(Long createBt) {
		this.createBt = createBt;
	}

	public Long getCreateEt() {
		return createEt;
	}

	public void setCreateEt(Long createEt) {
		this.createEt = createEt;
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

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
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

	public Integer getResourceCategoryCode() {
		return resourceCategoryCode;
	}

	public void setResourceCategoryCode(Integer resourceCategoryCode) {
		this.resourceCategoryCode = resourceCategoryCode;
	}

	public String getBookCode() {
		return bookCode;
	}

	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}

	public Long getPublishBt() {
		return publishBt;
	}

	public void setPublishBt(Long publishBt) {
		this.publishBt = publishBt;
	}

	public Long getPublishEt() {
		return publishEt;
	}

	public void setPublishEt(Long publishEt) {
		this.publishEt = publishEt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * 排序字段.
	 * 
	 * @author wlche
	 *
	 */
	public enum OrderType implements Valueable {
		DEFAULT(0), NAME(1), CATEGORY(2), TEXTBOOK(3), PUBLISHTIME(4);
		private int value;

		OrderType(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			return value;
		}
	}
}
