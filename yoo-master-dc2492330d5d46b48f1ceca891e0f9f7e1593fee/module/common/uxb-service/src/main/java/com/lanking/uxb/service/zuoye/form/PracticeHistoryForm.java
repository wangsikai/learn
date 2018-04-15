package com.lanking.uxb.service.zuoye.form;

import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.type.Biz;

/**
 * 增加或更新练习历史提交表单
 * 
 * @since yoomath(mobile) V1.1.0
 * @author wangsenhao
 *
 */
public class PracticeHistoryForm {

	// 名称
	private String name;

	// 用户ID
	private long userId;

	// 业务类型
	private Biz biz;

	// 业务ID
	private long bizId;

	// 正确率
	private BigDecimal rightRate;

	// 完成率
	private BigDecimal completionRate;

	// 创建时间
	private Date createAt;

	// 更新时间
	private Date updateAt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Biz getBiz() {
		return biz;
	}

	public void setBiz(Biz biz) {
		this.biz = biz;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
