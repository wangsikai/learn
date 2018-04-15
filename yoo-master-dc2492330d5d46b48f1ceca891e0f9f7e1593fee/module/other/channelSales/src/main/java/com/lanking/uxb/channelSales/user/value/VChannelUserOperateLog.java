package com.lanking.uxb.channelSales.user.value;

import java.util.Date;

import com.lanking.cloud.domain.support.channelSales.user.ChannelUserOperateLogType;

/**
 * 用户操作记录
 * 
 * @author wangsenhao
 *
 */
public class VChannelUserOperateLog {
	private Date createAt;
	// 创建人
	private String creatName;

	private ChannelUserOperateLogType type;

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getCreatName() {
		return creatName;
	}

	public void setCreatName(String creatName) {
		this.creatName = creatName;
	}

	public ChannelUserOperateLogType getType() {
		return type;
	}

	public void setType(ChannelUserOperateLogType type) {
		this.type = type;
	}

}
