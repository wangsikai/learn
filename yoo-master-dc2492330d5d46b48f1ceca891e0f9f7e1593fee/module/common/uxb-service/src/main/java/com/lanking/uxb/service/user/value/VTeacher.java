package com.lanking.uxb.service.user.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月15日
 *
 */
public class VTeacher implements Serializable {

	private static final long serialVersionUID = -2681304771278661129L;

	private VSubject subject;
	private VPhase phase;
	private VTextbookCategory textbookCategory;
	private VTextbook textbook;

	private String teachYear = StringUtils.EMPTY;
	private Date workAt;
	private String title = StringUtils.EMPTY;
	private Integer titleCode;
	private String duty = StringUtils.EMPTY;
	private Integer dutyCode;
	private String schoolName;

	public VSubject getSubject() {
		return subject;
	}

	public void setSubject(VSubject subject) {
		this.subject = subject;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public VTextbookCategory getTextbookCategory() {
		return textbookCategory;
	}

	public void setTextbookCategory(VTextbookCategory textbookCategory) {
		this.textbookCategory = textbookCategory;
	}

	public VTextbook getTextbook() {
		return textbook;
	}

	public void setTextbook(VTextbook textbook) {
		this.textbook = textbook;
	}

	public String getTeachYear() {
		return teachYear;
	}

	public void setTeachYear(String teachYear) {
		this.teachYear = teachYear;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDuty() {
		if (StringUtils.isBlank(duty)) {
			return "老师";
		}
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Date getWorkAt() {
		return workAt;
	}

	public void setWorkAt(Date workAt) {
		this.workAt = workAt;
	}

	public Integer getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(Integer titleCode) {
		this.titleCode = titleCode;
	}

	public Integer getDutyCode() {
		return dutyCode;
	}

	public void setDutyCode(Integer dutyCode) {
		this.dutyCode = dutyCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

}
