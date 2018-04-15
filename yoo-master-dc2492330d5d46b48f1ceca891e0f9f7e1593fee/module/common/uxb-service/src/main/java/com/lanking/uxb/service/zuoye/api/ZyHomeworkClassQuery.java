package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 班级查询条件
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月23日
 */
public class ZyHomeworkClassQuery {
	private long teacherId;
	private Status status;

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
