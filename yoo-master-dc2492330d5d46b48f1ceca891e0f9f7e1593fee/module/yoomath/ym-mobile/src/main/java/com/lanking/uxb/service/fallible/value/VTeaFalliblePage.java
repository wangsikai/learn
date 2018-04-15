package com.lanking.uxb.service.fallible.value;

import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.zuoye.value.VTeacherFallibleQuestion;

public class VTeaFalliblePage extends VPage<VTeacherFallibleQuestion> {

	private static final long serialVersionUID = 7618406535438787443L;

	private long endTime;

	private long cursor;

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getCursor() {
		return cursor;
	}

	public void setCursor(long cursor) {
		this.cursor = cursor;
	}

}
