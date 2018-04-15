package com.lanking.uxb.service.school.form;

import java.util.List;

import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderType;

/**
 * 提交请求处理form
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
public class QuestionRecordOrderCreateForm {
	// 教材版本码
	private int categoryCode;
	// 类型
	private QuestionRecordOrderType type;
	// 手机号
	private String mobile;
	// 录入描述
	private String description;
	// 附件
	private List<Long> files;

	public int getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public QuestionRecordOrderType getType() {
		return type;
	}

	public void setType(QuestionRecordOrderType type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getFiles() {
		return files;
	}

	public void setFiles(List<Long> files) {
		this.files = files;
	}

}
