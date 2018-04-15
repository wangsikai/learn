package com.lanking.uxb.zycon.mall.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 期别相关vo
 * 
 * @author wangsenhao
 *
 */
public class VZycCoinsLotterySeason implements Serializable {

	private static final long serialVersionUID = 5956579593186937702L;

	private Long id;

	// 开始时间
	private Date startTime;

	// 结束时间
	private Date endTime;

	// 表示一个用户每天可以参加的次数
	private int userJoinTimes;

	// 表示标题
	private String title;

	private Status status;

	// 表示是不是每周一次
	private Boolean everyWeek = true;

	// 抽奖活动名称
	private String name;

	// 面向用户
	private UserType userType;

	// 手机链接地址
	private String mobileUrl;

	// web端链接地址
	private String webUrl;
	// 活动编号
	private Integer code;

	// 普通还是活动
	private CoinsLotteryType type;

	/**
	 * 每人每天的中奖次数限制
	 */
	private Integer userAwardsTimes;
	/**
	 * 奖池每天可以中奖的次数限制
	 */
	private Integer awardsTimes;

	/**
	 * 某个用户${mustAwardsTimes-1}次没有中奖,第${mustAwardsTimes}必定中奖,-1表示无此设置
	 */
	private Integer mustAwardsTimes;

	/**
	 * 对应mustAwardsTimes的奖品坑位
	 */
	private Integer mustAwardsGoods;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getUserJoinTimes() {
		return userJoinTimes;
	}

	public void setUserJoinTimes(int userJoinTimes) {
		this.userJoinTimes = userJoinTimes;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getEveryWeek() {
		return everyWeek;
	}

	public void setEveryWeek(Boolean everyWeek) {
		this.everyWeek = everyWeek;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMobileUrl() {
		return mobileUrl;
	}

	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public CoinsLotteryType getType() {
		return type;
	}

	public void setType(CoinsLotteryType type) {
		this.type = type;
	}

	public Integer getAwardsTimes() {
		return awardsTimes;
	}

	public void setAwardsTimes(Integer awardsTimes) {
		this.awardsTimes = awardsTimes;
	}

	public Integer getMustAwardsTimes() {
		return mustAwardsTimes;
	}

	public void setMustAwardsTimes(Integer mustAwardsTimes) {
		this.mustAwardsTimes = mustAwardsTimes;
	}

	public Integer getMustAwardsGoods() {
		return mustAwardsGoods;
	}

	public void setMustAwardsGoods(Integer mustAwardsGoods) {
		this.mustAwardsGoods = mustAwardsGoods;
	}

	public Integer getUserAwardsTimes() {
		return userAwardsTimes;
	}

	public void setUserAwardsTimes(Integer userAwardsTimes) {
		this.userAwardsTimes = userAwardsTimes;
	}

}
