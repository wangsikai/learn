package com.lanking.uxb.service.examPaper.form;

import java.util.List;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;

/**
 * 教师组卷列表查询表单
 * 
 * @author zemin.song
 */
public class CustomExamPaperQuery {
	// 教师ID
	private Long teachId;
	// 状态
	private CustomExampaperStatus status;
	private List<CustomExampaperStatus> statuses;
	// 关键字
	private String key;

	private Integer pageSize = 20;
	private Integer page = 1;

	public CustomExampaperStatus getStatus() {
		return status;
	}

	public void setStatus(CustomExampaperStatus status) {
		this.status = status;
	}

	public List<CustomExampaperStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<CustomExampaperStatus> statuses) {
		this.statuses = statuses;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Long getTeachId() {
		return teachId;
	}

	public void setTeachId(Long teachId) {
		this.teachId = teachId;
	}

}
