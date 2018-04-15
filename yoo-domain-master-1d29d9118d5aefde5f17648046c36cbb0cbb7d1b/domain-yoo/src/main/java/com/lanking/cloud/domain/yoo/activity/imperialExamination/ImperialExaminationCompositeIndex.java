package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;

/**
 * 科举考试综合表现指数<br>
 * 例：name1*rate1+name2*rate2+...+name100*rate100
 * 
 * @since 3.9.4
 * @author senhao.wang
 * @version 2017年3月30日
 */
public class ImperialExaminationCompositeIndex implements Serializable {

	private static final long serialVersionUID = -7176167757632824060L;

	/**
	 * 综合表现参数，例：平均提交率、平均正确率
	 */
	private String name;

	/**
	 * 所占比例
	 */
	private double rate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

}
