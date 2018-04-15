package com.lanking.uxb.service.report.value;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VStudentPaperSituation implements Serializable {
	private static final long serialVersionUID = -5504930281596697157L;

	/**
	 * 作业数量.
	 */
	private Integer hkCount;

	/**
	 * 习题数量.
	 */
	private Integer questionCount;

	/**
	 * 平均完成率
	 */
	private Integer completionRate;

	/**
	 * 平均正确率.
	 */
	private Integer rightRate;

	/**
	 * 自主练习平均题量.
	 */
	private Integer selfDoCount;
}
