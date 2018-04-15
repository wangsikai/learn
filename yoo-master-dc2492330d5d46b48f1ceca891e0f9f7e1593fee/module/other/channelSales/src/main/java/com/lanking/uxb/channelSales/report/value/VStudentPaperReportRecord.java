package com.lanking.uxb.channelSales.report.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportStatus;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

import lombok.Getter;
import lombok.Setter;

/**
 * 学业报告记录.
 * 
 * @author wlche
 *
 */
@Getter
@Setter
public class VStudentPaperReportRecord implements Serializable {
	private static final long serialVersionUID = -776639847450051184L;

	private long id;
	private long operator;
	private long classId;
	private Date startDate;
	private Date endDate;
	private boolean allStudent = true;
	private StudentPaperReportStatus status;
	private Integer downloadCount;
	private Date createAt;
	private boolean isSingleMonth = false;
	private boolean isNoDatas = false; // 是否所有指定学生都没有数据

	private VHomeworkClazz homeworkClazz;
}
