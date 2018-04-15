package com.lanking.uxb.service.index.form;

import java.util.List;

/**
 * 教师首页请求参数form
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月18日
 */
public class TeaIndexForm {
	private Long clazzId;
	private List<Long> clazzIds;

	public Long getClazzId() {
		return clazzId;
	}

	public void setClazzId(Long clazzId) {
		this.clazzId = clazzId;
	}

	public List<Long> getClazzIds() {
		return clazzIds;
	}

	public void setClazzIds(List<Long> clazzIds) {
		this.clazzIds = clazzIds;
	}

}
