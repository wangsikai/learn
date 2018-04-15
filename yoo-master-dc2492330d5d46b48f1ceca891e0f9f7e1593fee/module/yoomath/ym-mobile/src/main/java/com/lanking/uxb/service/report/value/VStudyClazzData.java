package com.lanking.uxb.service.report.value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 班级学情分析---单个班级对象
 * 
 * @author wangsenhao
 *
 */
public class VStudyClazzData implements Serializable {

	private static final long serialVersionUID = -8060145328132100974L;

	private long clazzId;
	private String clazzName;
	/**
	 * 作业总数
	 */
	private long homeWorkNum = 0;
	/**
	 * 班级正确率
	 */
	private BigDecimal rightRate;
	/**
	 * 班级正确率title,区分前台显示的0还是--
	 */
	private String rightRateTitle;
	/**
	 * 提交率
	 */
	private BigDecimal commitRate;
	/**
	 * 班级提交率title
	 */
	private String commitRateTitle;
	/**
	 * 班级人数
	 */
	private Integer studentNum;

	private String url;

	public long getClazzId() {
		return clazzId;
	}

	public void setClazzId(long clazzId) {
		this.clazzId = clazzId;
	}

	public String getClazzName() {
		return clazzName;
	}

	public void setClazzName(String clazzName) {
		this.clazzName = clazzName;
	}

	public long getHomeWorkNum() {
		return homeWorkNum;
	}

	public void setHomeWorkNum(long homeWorkNum) {
		this.homeWorkNum = homeWorkNum;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCommitRate() {
		return commitRate;
	}

	public void setCommitRate(BigDecimal commitRate) {
		this.commitRate = commitRate;
	}

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public String getCommitRateTitle() {
		return commitRateTitle;
	}

	public void setCommitRateTitle(String commitRateTitle) {
		this.commitRateTitle = commitRateTitle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
