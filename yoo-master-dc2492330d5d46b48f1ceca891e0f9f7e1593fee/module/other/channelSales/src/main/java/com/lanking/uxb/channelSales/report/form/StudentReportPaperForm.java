package com.lanking.uxb.channelSales.report.form;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 生成学生纸质报告参数.
 * 
 * @author wlche
 *
 */
@Getter
@Setter
public class StudentReportPaperForm implements Serializable {
	private static final long serialVersionUID = 7970642887615719320L;

	private Long classId;
	private String startDate;
	private String endDate;
	private boolean allStudent = true;
	private List<Long> studentIds;
}
