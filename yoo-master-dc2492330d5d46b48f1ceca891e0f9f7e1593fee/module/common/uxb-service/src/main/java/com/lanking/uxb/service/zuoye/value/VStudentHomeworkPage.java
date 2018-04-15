package com.lanking.uxb.service.zuoye.value;

import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.resources.value.VStudentHomework;

public class VStudentHomeworkPage extends VPage<VStudentHomework> {

	private static final long serialVersionUID = 7813892113651465328L;

	private String motto;
	// 是否显示给家长的一封信
	private Boolean showParentLetter;

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}

	public Boolean getShowParentLetter() {
		return showParentLetter;
	}

	public void setShowParentLetter(Boolean showParentLetter) {
		this.showParentLetter = showParentLetter;
	}

}
