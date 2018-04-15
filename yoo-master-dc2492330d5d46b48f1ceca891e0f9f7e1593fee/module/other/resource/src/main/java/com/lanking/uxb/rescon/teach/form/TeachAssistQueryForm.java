package com.lanking.uxb.rescon.teach.form;

import java.io.Serializable;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.uxb.rescon.book.form.BookQueryForm.OrderType;

/**
 * 教辅查询.
 * 
 * @author zemin.Song
 */
public class TeachAssistQueryForm implements Serializable {

	private static final long serialVersionUID = -5564565799783388467L;

	/**
	 * 查询关键字.
	 */
	private String key;

	/**
	 * 创建人.
	 */
	private Long createId;

	/**
	 * 教辅状态.
	 */
	private TeachAssistStatus status;

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
	 * 版本
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
	 * 审核状态
	 */
	private Integer teachAssistStatus;

	/**
	 * 教辅名称.
	 * 
	 * @since rescon UE v1.2.6
	 */
	private String name;

	/**
	 * 排序字段.
	 */
	private OrderType orderType;

	/**
	 * 排序方向.
	 */
	private Direction direction;
	/**
	 * 状态
	 */
	private Status delStatus;

	/**
	 * 分页
	 */
	private Integer pageSize = 20;
	private Integer page = 1;

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

	public TeachAssistStatus getStatus() {
		return status;
	}

	public void setStatus(TeachAssistStatus status) {
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

	public Integer getTeachAssistStatus() {
		return teachAssistStatus;
	}

	public void setTeachAssistStatus(Integer teachAssistStatus) {
		this.teachAssistStatus = teachAssistStatus;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
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

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

}
