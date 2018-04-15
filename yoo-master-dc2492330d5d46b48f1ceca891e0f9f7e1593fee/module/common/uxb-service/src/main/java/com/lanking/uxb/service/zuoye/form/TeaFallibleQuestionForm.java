package com.lanking.uxb.service.zuoye.form;

import java.math.BigDecimal;
import java.util.List;

/**
 * 教师错题查询表单
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月17日
 */
public class TeaFallibleQuestionForm {
	private Integer pageNo;
	private Integer size;
	private BigDecimal leRightRate;
	private BigDecimal reRightRate;
	private BigDecimal leDifficulty;
	private BigDecimal reDifficulty;
	private Integer textbookCode;
	private List<Long> sectionCodes;
	private Boolean isUpdateAtDesc;
	private Boolean isCreateAtDesc;
	private Boolean isDifficultyDesc;
	private Boolean isRightRateDesc;
	/**
	 * 题型.
	 */
	private Long typeCode;
	/**
	 * 时间范围:不设置表示全部,1:表示最近一个月2:表示本学期3:表示本学年(跟设计交流中,感觉这个随便可能变,具有很大的不确定性,所以不用枚举了)
	 */
	private Integer timeRange;

	public Integer getPageNo() {
		if (pageNo == null || pageNo < 0) {
			return 1;
		}
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getSize() {
		if (size == null || size > 40 || size < 0) {
			return 40;
		}
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public BigDecimal getLeRightRate() {
		return leRightRate;
	}

	public void setLeRightRate(BigDecimal leRightRate) {
		this.leRightRate = leRightRate;
	}

	public BigDecimal getReRightRate() {
		return reRightRate;
	}

	public void setReRightRate(BigDecimal reRightRate) {
		this.reRightRate = reRightRate;
	}

	public BigDecimal getLeDifficulty() {
		return leDifficulty;
	}

	public void setLeDifficulty(BigDecimal leDifficulty) {
		this.leDifficulty = leDifficulty;
	}

	public BigDecimal getReDifficulty() {
		return reDifficulty;
	}

	public void setReDifficulty(BigDecimal reDifficulty) {
		this.reDifficulty = reDifficulty;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Boolean getIsUpdateAtDesc() {
		return isUpdateAtDesc;
	}

	public void setIsUpdateAtDesc(Boolean isUpdateAtDesc) {
		this.isUpdateAtDesc = isUpdateAtDesc;
	}

	public Boolean getIsCreateAtDesc() {
		return isCreateAtDesc;
	}

	public void setIsCreateAtDesc(Boolean isCreateAtDesc) {
		this.isCreateAtDesc = isCreateAtDesc;
	}

	public Boolean getIsDifficultyDesc() {
		return isDifficultyDesc;
	}

	public void setIsDifficultyDesc(Boolean isDifficultyDesc) {
		this.isDifficultyDesc = isDifficultyDesc;
	}

	public Boolean getIsRightRateDesc() {
		return isRightRateDesc;
	}

	public void setIsRightRateDesc(Boolean isRightRateDesc) {
		this.isRightRateDesc = isRightRateDesc;
	}

	public Integer getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}

	public Long getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Long typeCode) {
		this.typeCode = typeCode;
	}

}
