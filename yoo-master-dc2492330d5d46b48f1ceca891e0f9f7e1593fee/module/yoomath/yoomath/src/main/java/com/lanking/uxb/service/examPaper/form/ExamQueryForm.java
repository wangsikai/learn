package com.lanking.uxb.service.examPaper.form;

import java.util.List;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;

/**
 * 试卷查询处理form
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class ExamQueryForm {
	// 关键字
	private String key;
	// 资源类型
	private ResourcesGoodsType goodsType;
	// 阶段码
	private Integer phaseCode;
	// 学科码
	private Integer subjectCode;
	// 学校id
	private Long schoolId;
	// 分页条件
	private Integer page;
	// 当前页最多条数
	private Integer pageSize;
	// 排序key
	private String orderBy;
	// 地区码
	private Long districtCode;
	// 试卷分类
	private List<Long> categoryCodes;
	// 年份
	private Integer year;
	// 当为true的时候是降序,当为false的时候是升序
	private Boolean order;
	// 上架状态
	private ResourcesGoodsStatus status;
	// 是否推荐
	private Boolean isRecommend;
	// 章节
	private List<Long> sectionCodes;
	// 创将用户Id
	private Long createId;

	// 是否随机
	private Boolean isRandom;

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

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
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

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}

	public List<Long> getCategoryCodes() {
		return categoryCodes;
	}

	public void setCategoryCodes(List<Long> categoryCodes) {
		this.categoryCodes = categoryCodes;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
	}

	public ResourcesGoodsStatus getStatus() {
		return status;
	}

	public void setStatus(ResourcesGoodsStatus status) {
		this.status = status;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public ResourcesGoodsType getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(ResourcesGoodsType goodsType) {
		this.goodsType = goodsType;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Boolean getIsRandom() {
		return isRandom;
	}

	public void setIsRandom(Boolean isRandom) {
		this.isRandom = isRandom;
	}

}
