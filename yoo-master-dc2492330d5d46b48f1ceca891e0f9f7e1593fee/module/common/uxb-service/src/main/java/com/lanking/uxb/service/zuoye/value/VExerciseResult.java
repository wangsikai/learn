package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.honor.value.VUserReward;

/**
 * 练习提交结果(章节练习、每日练、智能试卷)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月30日
 */
public class VExerciseResult implements Serializable {

	private static final long serialVersionUID = -3062542440090600226L;

	// 1:章节练习2:每日练3:智能试卷
	private int type;
	// 正确率
	private BigDecimal rightRate;
	private String rightRateTitle;
	// 题目ID集合
	private List<Long> qIds;
	// 题目批改结果集合
	private List<HomeworkAnswerResult> results;
	// 已答未答
	private List<Boolean> dones;
	// 智能试卷提交时间
	private Date commitAt;

	private VUserReward userReward;
	// 目前只供章节练习提交后id
	private Long exeId;

	public VExerciseResult() {
		super();
	}

	public VExerciseResult(int type) {
		super();
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(String.valueOf(getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public List<HomeworkAnswerResult> getResults() {
		return results;
	}

	public void setResults(List<HomeworkAnswerResult> results) {
		this.results = results;
	}

	public List<Boolean> getDones() {
		return dones;
	}

	public void setDones(List<Boolean> dones) {
		this.dones = dones;
	}

	public Date getCommitAt() {
		return commitAt;
	}

	public void setCommitAt(Date commitAt) {
		this.commitAt = commitAt;
	}

	public VUserReward getUserReward() {
		return userReward;
	}

	public void setUserReward(VUserReward userReward) {
		this.userReward = userReward;
	}

	public Long getExeId() {
		return exeId;
	}

	public void setExeId(Long exeId) {
		this.exeId = exeId;
	}
}
