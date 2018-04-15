package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;

/**
 * 科举考试的奖品类型
 * 
 * @since 3.9.4
 * @author senhao.wang
 * @version 2017年3月30日
 */
public class ImperialExaminationAwardType implements Serializable {

	private static final long serialVersionUID = 807118695454797320L;
	/**
	 * 奖品级别
	 */
	private Integer awardLevel;
	/**
	 * 一等奖、二等奖、三等奖等
	 */
	private String prize;
	/**
	 * 人数
	 */
	private int num;
	/**
	 * 奖品名称
	 */
	private String prizeName;

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public Integer getAwardLevel() {
		return awardLevel;
	}

	public void setAwardLevel(Integer awardLevel) {
		this.awardLevel = awardLevel;
	}

}
