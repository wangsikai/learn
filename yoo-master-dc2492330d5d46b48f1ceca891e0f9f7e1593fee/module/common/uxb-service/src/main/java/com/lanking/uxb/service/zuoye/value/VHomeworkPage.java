package com.lanking.uxb.service.zuoye.value;

import java.util.List;

import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.code.value.VClazz;
import com.lanking.uxb.service.resources.value.VHomework;

public class VHomeworkPage extends VPage<VHomework> {

	private static final long serialVersionUID = 4807393404356245623L;

	private List<VClazz> clazzs;
	private List<VHomeworkClazz> homeworkClazzs;

	public List<VClazz> getClazzs() {
		return clazzs;
	}

	public void setClazzs(List<VClazz> clazzs) {
		this.clazzs = clazzs;
	}

	public List<VHomeworkClazz> getHomeworkClazzs() {
		return homeworkClazzs;
	}

	public void setHomeworkClazzs(List<VHomeworkClazz> homeworkClazzs) {
		this.homeworkClazzs = homeworkClazzs;
	}

}
