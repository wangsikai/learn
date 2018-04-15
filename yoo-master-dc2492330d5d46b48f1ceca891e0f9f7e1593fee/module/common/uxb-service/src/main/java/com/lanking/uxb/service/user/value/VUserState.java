package com.lanking.uxb.service.user.value;

import java.io.Serializable;

/**
 * 用户统计信息VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public class VUserState implements Serializable {

	private static final long serialVersionUID = 6308583225261290937L;

	private long postCount = 0;
	private long followCount = 0;
	private long fanCount = 0;
	private long groupCount = 0;

	private long selfResCount = 0;
	private long buyResCount = 0;

	private long courseCount = 0;
	/**
	 * 贡献数
	 * 
	 * @since2.1
	 */
	private long contributeCount = 0;
	/**
	 * 圈子数
	 */
	private long circleCount = 0;

	/**
	 * 收藏题目的数量(yoomath个性化的东西,目前不在convert里面初始化)
	 */
	private long questionCollectCount = 0;

	/**
	 * 临时习题的数量(yoomath个性化的东西,目前不在convert里面初始化)
	 */
	private long practiceHistoryCount = 0;

	public long getContributeCount() {
		return contributeCount;
	}

	public void setContributeCount(long contributeCount) {
		this.contributeCount = contributeCount;
	}

	public long getPostCount() {
		return postCount;
	}

	public void setPostCount(long postCount) {
		this.postCount = postCount;
	}

	public long getFollowCount() {
		return followCount;
	}

	public void setFollowCount(long followCount) {
		this.followCount = followCount;
	}

	public long getFanCount() {
		return fanCount;
	}

	public void setFanCount(long fanCount) {
		this.fanCount = fanCount;
	}

	public long getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(long groupCount) {
		this.groupCount = groupCount;
	}

	public long getSelfResCount() {
		return selfResCount;
	}

	public void setSelfResCount(long selfResCount) {
		this.selfResCount = selfResCount;
	}

	public long getBuyResCount() {
		return buyResCount;
	}

	public void setBuyResCount(long buyResCount) {
		this.buyResCount = buyResCount;
	}

	public long getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(long courseCount) {
		this.courseCount = courseCount;
	}

	public long getCircleCount() {
		return circleCount;
	}

	public void setCircleCount(long circleCount) {
		this.circleCount = circleCount;
	}

	public long getQuestionCollectCount() {
		return questionCollectCount;
	}

	public void setQuestionCollectCount(long questionCollectCount) {
		this.questionCollectCount = questionCollectCount;
	}

	public long getPracticeHistoryCount() {
		return practiceHistoryCount;
	}

	public void setPracticeHistoryCount(long practiceHistoryCount) {
		this.practiceHistoryCount = practiceHistoryCount;
	}

}
