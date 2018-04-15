package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;

/**
 * 作业总体统计知识点统计VO
 * 
 * @since yoomath V1.4
 * @author wangsenhao
 *
 */
public class VHkStuClazzKnowpointStat implements Serializable {

	private static final long serialVersionUID = -825780006547651081L;
	/**
	 * 知识点code
	 */
	private Long code;
	/**
	 * 知识点名
	 */
	private String knowPointName;
	/**
	 * 做题数
	 */
	private Long total;
	/**
	 * 错题数
	 */
	private Long wrongNum;
	/**
	 * 班级平均正确率
	 */
	private Long classRightRate;
	/**
	 * 我的正确率
	 */
	private Long stuRightRate;
	/**
	 * 学生班级排名
	 */
	private Integer stuRank;

	public String getKnowPointName() {
		return knowPointName;
	}

	public void setKnowPointName(String knowPointName) {
		this.knowPointName = knowPointName;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getWrongNum() {
		return wrongNum;
	}

	public void setWrongNum(Long wrongNum) {
		this.wrongNum = wrongNum;
	}

	public Long getClassRightRate() {
		return classRightRate;
	}

	public void setClassRightRate(Long classRightRate) {
		this.classRightRate = classRightRate;
	}

	public Long getStuRightRate() {
		return stuRightRate;
	}

	public void setStuRightRate(Long stuRightRate) {
		this.stuRightRate = stuRightRate;
	}

	public Integer getStuRank() {
		return stuRank;
	}

	public void setStuRank(Integer stuRank) {
		this.stuRank = stuRank;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}
}
