package com.lanking.uxb.rescon.book.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.service.code.value.VSchool;

/**
 * 书本VO.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月23日
 */
public class VBook implements Serializable {
	private static final long serialVersionUID = -6172382489482872420L;

	private Long id;

	/**
	 * 主版本.
	 */
	private VBookVersion mainVersion;

	/**
	 * 副版本.
	 */
	private VBookVersion deputyVersion;

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
	private VVendorUser creator;

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

	public VBookVersion getMainVersion() {
		return mainVersion;
	}

	public void setMainVersion(VBookVersion mainVersion) {
		this.mainVersion = mainVersion;
	}

	public VBookVersion getDeputyVersion() {
		return deputyVersion;
	}

	public void setDeputyVersion(VBookVersion deputyVersion) {
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

	public VVendorUser getCreator() {
		return creator;
	}

	public void setCreator(VVendorUser creator) {
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
