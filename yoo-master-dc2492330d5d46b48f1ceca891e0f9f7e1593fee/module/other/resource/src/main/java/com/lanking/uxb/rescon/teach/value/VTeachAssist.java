package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.service.code.value.VSchool;

/**
 * 教辅VO.
 * 
 * @author zemin.Song
 */
public class VTeachAssist implements Serializable {

	private static final long serialVersionUID = 206620549576027529L;

	private Long id;

	/**
	 * 主版本.
	 */
	private VTeachAssistVersion mainVersion;

	/**
	 * 副版本.
	 */
	private VTeachAssistVersion deputyVersion;

	/**
	 * 版本个数.
	 */
	private int num;

	/**
	 * 创建时间.
	 */
	private Date createAt;

	/**
	 * 创建人.
	 */
	private VendorUser creator;

	/**
	 * 书本状态.
	 */
	private Status status;

	/**
	 * 书本学校.
	 */
	private VSchool school;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VTeachAssistVersion getMainVersion() {
		return mainVersion;
	}

	public void setMainVersion(VTeachAssistVersion mainVersion) {
		this.mainVersion = mainVersion;
	}

	public VTeachAssistVersion getDeputyVersion() {
		return deputyVersion;
	}

	public void setDeputyVersion(VTeachAssistVersion deputyVersion) {
		this.deputyVersion = deputyVersion;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public VendorUser getCreator() {
		return creator;
	}

	public void setCreator(VendorUser creator) {
		this.creator = creator;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public VSchool getSchool() {
		return school;
	}

	public void setSchool(VSchool school) {
		this.school = school;
	}

}
