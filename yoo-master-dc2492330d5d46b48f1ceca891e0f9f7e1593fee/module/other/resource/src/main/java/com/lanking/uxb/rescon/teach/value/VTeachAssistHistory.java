package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistHistory.OperateType;

/**
 * 教辅操作记录VO
 * 
 * @author wangsenhao
 *
 */
public class VTeachAssistHistory implements Serializable {

	private static final long serialVersionUID = 5582113216937169450L;

	/**
	 * 教辅ID.
	 */
	private Long teachAssistId;

	/**
	 * 操作状态.
	 */
	private OperateType type;

	/**
	 * 操作人.
	 */
	private String creator;

	/**
	 * 操作时间.
	 */
	private Date createAt;

	/**
	 * 操作版本.
	 */
	private Integer version;

	public Long getTeachAssistId() {
		return teachAssistId;
	}

	public void setTeachAssistId(Long teachAssistId) {
		this.teachAssistId = teachAssistId;
	}

	public OperateType getType() {
		return type;
	}

	public void setType(OperateType type) {
		this.type = type;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

}
