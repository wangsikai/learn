package com.lanking.uxb.service.imperial.form;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;

/**
 * 科举考试报名提交信息
 * 
 * @author wangsenhao
 *
 */
public class ImperialExaminationActivityUserForm {

	/**
	 * 选择的年级
	 */
	private ImperialExaminationGrade grade;
	/**
	 * 报名的教师姓名，用户自己填的
	 */
	private String name;
	/**
	 * 报名的手机号
	 */
	private String mobile;
	/**
	 * 报名的班级
	 */
	private List<Long> classList;

	private Long userId;
	private Long code;
	/**
	 * 选择的版本
	 */
	private Integer textbookCategoryCode;
	/**
	 * 考场
	 */
	private Integer room;

	public ImperialExaminationGrade getGrade() {
		return grade;
	}

	public void setGrade(ImperialExaminationGrade grade) {
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<Long> getClassList() {
		return classList;
	}

	public void setClassList(List<Long> classList) {
		this.classList = classList;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

}
