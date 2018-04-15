package com.lanking.cloud.domain.yoo.activity.holiday001;

import java.io.Serializable;
import java.util.List;

/**
 * 假期活动01-配置
 * 
 * <pre>
 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日)
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
public final class HolidayActivity01Cfg implements Serializable {

	private static final long serialVersionUID = 4613654250338968839L;

	/**
	 * 活动代码
	 */
	private Long code;

	/**
	 * 可以参与活动的版本代码(2:初中3高中)
	 */
	private List<Integer> textbookCategoryCodes2;
	private List<Integer> textbookCategoryCodes3;

	/**
	 * 可以参与活动的年级(2:初中3高中)
	 */
	private List<HolidayActivity01Grade> grades2;
	private List<HolidayActivity01Grade> grades3;

	/**
	 * 时间段
	 */
	private List<List<Long>> periods;

	/**
	 * 参与班级的最少学生人数
	 */
	private int minClassStudents;

	/**
	 * 一次作业获取的抽奖次数
	 */
	private int luckyDrawOneHomework;

	/**
	 * 提交率和对应获取抽奖次数的阀值（一一对应,初始化请倒序存放）
	 */
	private List<Integer> submitRateThreshold;
	private List<Integer> luckyDrawThreshold;

	/**
	 * 对应活动抽奖期别
	 */
	private Long seasonId;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public List<Integer> getTextbookCategoryCodes2() {
		return textbookCategoryCodes2;
	}

	public void setTextbookCategoryCodes2(List<Integer> textbookCategoryCodes2) {
		this.textbookCategoryCodes2 = textbookCategoryCodes2;
	}

	public List<Integer> getTextbookCategoryCodes3() {
		return textbookCategoryCodes3;
	}

	public void setTextbookCategoryCodes3(List<Integer> textbookCategoryCodes3) {
		this.textbookCategoryCodes3 = textbookCategoryCodes3;
	}

	public List<HolidayActivity01Grade> getGrades2() {
		return grades2;
	}

	public void setGrades2(List<HolidayActivity01Grade> grades2) {
		this.grades2 = grades2;
	}

	public List<HolidayActivity01Grade> getGrades3() {
		return grades3;
	}

	public void setGrades3(List<HolidayActivity01Grade> grades3) {
		this.grades3 = grades3;
	}

	public List<List<Long>> getPeriods() {
		return periods;
	}

	public void setPeriods(List<List<Long>> periods) {
		this.periods = periods;
	}

	public int getMinClassStudents() {
		return minClassStudents;
	}

	public void setMinClassStudents(int minClassStudents) {
		this.minClassStudents = minClassStudents;
	}

	public int getLuckyDrawOneHomework() {
		return luckyDrawOneHomework;
	}

	public void setLuckyDrawOneHomework(int luckyDrawOneHomework) {
		this.luckyDrawOneHomework = luckyDrawOneHomework;
	}

	public List<Integer> getSubmitRateThreshold() {
		return submitRateThreshold;
	}

	public void setSubmitRateThreshold(List<Integer> submitRateThreshold) {
		this.submitRateThreshold = submitRateThreshold;
	}

	public List<Integer> getLuckyDrawThreshold() {
		return luckyDrawThreshold;
	}

	public void setLuckyDrawThreshold(List<Integer> luckyDrawThreshold) {
		this.luckyDrawThreshold = luckyDrawThreshold;
	}

	public Long getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

}
