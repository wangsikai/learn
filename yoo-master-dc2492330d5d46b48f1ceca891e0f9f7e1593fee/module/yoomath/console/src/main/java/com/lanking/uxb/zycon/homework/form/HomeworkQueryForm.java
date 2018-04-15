package com.lanking.uxb.zycon.homework.form;

import java.util.List;

/**
 * 后台批改列表查询作业form
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
public class HomeworkQueryForm {
	// 当前页
	private int page = 1;
	// 当前分页大小
	private int size = 20;
	// 查询过滤作业状态类型
	private List<HomeworkQueryType> types;
	// 作业开始时间
	private String startTime;
	// 作业开始查询结束时间
	private String endTime;
	// 学校名
	private String schoolName;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<HomeworkQueryType> getTypes() {
		return types;
	}

	public void setTypes(List<HomeworkQueryType> types) {
		this.types = types;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
}
