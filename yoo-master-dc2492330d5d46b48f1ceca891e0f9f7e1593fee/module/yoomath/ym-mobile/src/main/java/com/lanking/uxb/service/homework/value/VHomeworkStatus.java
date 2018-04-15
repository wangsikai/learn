package com.lanking.uxb.service.homework.value;

import java.io.Serializable;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.sdk.util.StringUtils;

public class VHomeworkStatus implements Serializable {

	private static final long serialVersionUID = 1725113156364776702L;

	private HomeworkStatus status;

	private String statusName;

	public VHomeworkStatus() {
		super();
	}

	public VHomeworkStatus(HomeworkStatus status) {
		this.status = status;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public String getStatusName() {
		if (StringUtils.isBlank(statusName)) {
			String sn = "";
			if (getStatus() == HomeworkStatus.INIT) {
				sn = "待分发";
			} else if (getStatus() == HomeworkStatus.PUBLISH) {
				sn = "作业中";
			} else if (getStatus() == HomeworkStatus.NOT_ISSUE) {
				sn = "待批改";
			} else if (getStatus() == HomeworkStatus.ISSUED) {
				sn = "已下发";
			}
			setStatusName(sn);
		}
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
