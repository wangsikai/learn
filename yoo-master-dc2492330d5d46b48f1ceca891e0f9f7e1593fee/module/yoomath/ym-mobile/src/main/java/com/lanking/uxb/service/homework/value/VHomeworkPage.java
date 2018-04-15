package com.lanking.uxb.service.homework.value;

import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.resources.value.VHomework;

public class VHomeworkPage extends VPage<VHomework> {

	private static final long serialVersionUID = -7295064583041568316L;

	private long endTime;

	private boolean hasClazz; // 是否有班级

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isHasClazz() {
		return hasClazz;
	}

	public void setHasClazz(boolean hasClazz) {
		this.hasClazz = hasClazz;
	}

}
