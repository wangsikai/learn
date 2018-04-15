package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.util.Date;

/**
 * 科举考试流程时间表
 * 
 * @since 3.9.4
 * @author wangsenhao
 * @version 2017年3月30日
 */
public class ImperialExaminationProcessTime implements Serializable {

	private static final long serialVersionUID = -8785840824227476181L;

	private ImperialExaminationProcess process;
	private Date startTime;
	private Date endTime;

	public ImperialExaminationProcess getProcess() {
		return process;
	}

	public void setProcess(ImperialExaminationProcess process) {
		this.process = process;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
