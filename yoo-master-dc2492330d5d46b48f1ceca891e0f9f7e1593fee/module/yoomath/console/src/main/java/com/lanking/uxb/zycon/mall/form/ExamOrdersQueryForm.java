package com.lanking.uxb.zycon.mall.form;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;

/**
 * 试卷订单form
 *
 * @author zemin.song
 */
public class ExamOrdersQueryForm {
	// 关键字
	private String key;
	// 编号
	private String examCode;
	// 阶段码
	private Integer phaseCode;
	// 分页条件
	private Integer page;
	// 当前页最多条数
	private Integer pageSize;
	// 试卷分类
	private Integer category;
	// 开始时间
	private String startDate;
	// 结束时间
	private String endDate;
	// 资源商品类型
	private ResourcesGoodsType type;
	// 商品订单状态
	private GoodsOrderStatus status;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ResourcesGoodsType getType() {
		return type;
	}

	public void setType(ResourcesGoodsType type) {
		this.type = type;
	}

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

}
