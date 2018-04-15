package com.lanking.uxb.service.report.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

public class VStudentWeekReport implements Serializable {

	private static final long serialVersionUID = 4892015751053713706L;

	/**
	 * 用户ID
	 */
	private long userId;

	/**
	 * 开始时间
	 */
	private String startDate;

	/**
	 * 结束时间
	 */
	private String endDate;

	private Integer score;

	/**
	 * 完成率
	 */
	private BigDecimal completionRate;

	/**
	 * 完成率和上周比较浮动（与完成率排名浮动区分）
	 */
	private Integer completionRateFloat;

	/**
	 * 正确率
	 */
	private BigDecimal rightRate;

	/**
	 * 正确率和上周比较浮动（与正确率排名浮动区分）
	 */
	private Integer rightRateFloat;

	/**
	 * 本周班级数据
	 * 
	 * <pre>
	 * 	显示学生在1个班级中的排名，优先级按此排序：
	 * 1、本周有排名的班级
	 * 2、选择班级人数最多的班级
	 * 3、若人数相同，则最新加入的班级
	 * </pre>
	 */
	private Map<String, Object> thisWeekClassMap;

	private String rightRateClassRanks;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public Integer getCompletionRateFloat() {
		return completionRateFloat;
	}

	public void setCompletionRateFloat(Integer completionRateFloat) {
		this.completionRateFloat = completionRateFloat;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public Integer getRightRateFloat() {
		return rightRateFloat;
	}

	public void setRightRateFloat(Integer rightRateFloat) {
		this.rightRateFloat = rightRateFloat;
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

	public Map<String, Object> getThisWeekClassMap() {
		return thisWeekClassMap;
	}

	public void setThisWeekClassMap(Map<String, Object> thisWeekClassMap) {
		this.thisWeekClassMap = thisWeekClassMap;
	}

	public String getRightRateClassRanks() {
		return rightRateClassRanks;
	}

	public void setRightRateClassRanks(String rightRateClassRanks) {
		this.rightRateClassRanks = rightRateClassRanks;
	}

}
