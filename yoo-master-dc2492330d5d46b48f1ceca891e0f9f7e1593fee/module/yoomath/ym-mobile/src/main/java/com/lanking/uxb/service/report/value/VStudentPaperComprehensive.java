package com.lanking.uxb.service.report.value;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 综合统计数据.
 * 
 * @author wlche
 *
 */
@Getter
@Setter
public class VStudentPaperComprehensive implements Serializable {
	private static final long serialVersionUID = -5249440757434174859L;

	/**
	 * 班级综合情况.
	 */
	private VStudentPaperSituation clazzSituation;

	/**
	 * 个人综合情况.
	 */
	private VStudentPaperSituation stuSituation;
}
