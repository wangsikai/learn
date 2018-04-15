package com.lanking.uxb.rescon.basedata.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateType;

/**
 * 针对性训练操作记录
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public class VSpecialTrainingOperateLog implements Serializable {

	private static final long serialVersionUID = -4158480811967784065L;

	private String userName;
	private SpecialTrainingOperateType operateType;
	private Date createAt;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SpecialTrainingOperateType getOperateType() {
		return operateType;
	}

	public void setOperateType(SpecialTrainingOperateType operateType) {
		this.operateType = operateType;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
