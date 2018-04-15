package com.lanking.uxb.rescon.exam.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory.OperateType;

public class VOperationUser implements Serializable {

	private static final long serialVersionUID = -1955869156253850545L;

	private String name;
	private Date time;
	private OperateType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public OperateType getType() {
		return type;
	}

	public void setType(OperateType type) {
		this.type = type;
	}

}
