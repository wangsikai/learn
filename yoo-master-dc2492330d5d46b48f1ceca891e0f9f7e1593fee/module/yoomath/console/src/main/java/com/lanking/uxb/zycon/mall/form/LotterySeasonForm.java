package com.lanking.uxb.zycon.mall.form;

import java.util.List;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 后台抽奖管理提交--抽奖期别相关
 * 
 * @author wangsenhao
 *
 */
public class LotterySeasonForm {

	private Long id;

	// 开始时间
	private String startTime;

	// 结束时间
	private String endTime;

	// 表示一个用户每天可以参加的次数
	private int userJoinTimes;

	// 表示状态
	private Status status = Status.ENABLED;

	// 表示是不是每周一次
	private Boolean everyWeek = true;

	private List<LotteryGoodsForm> goodsList;

	private String list;

	private Long userId;
	// 活动名称
	private String activeName;
	// 活动面向用户
	private UserType userType;
	// 是编辑界面还是新增界面
	private String flag;
	// 活动编码，编辑时改变时间会返回，其他情况不返回
	private Integer code;
	
	// 阶段1开始时间
	private String phase1Start;

	// 阶段1结束时间
	private String phase1End;
	
	// 阶段2开始时间
	private String phase2Start;

	// 阶段2结束时间
	private String phase2End;
	
	// 阶段3开始时间
	private String phase3Start;

	// 阶段3结束时间
	private String phase3End;
	
	// 阶段4开始时间
	private String phase4Start;

	// 阶段5结束时间
	private String phase4End;

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

	/**
	 * 抽奖类型,原来没有传值的就是普通
	 */
	private CoinsLotteryType type = CoinsLotteryType.COMMON;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getUserJoinTimes() {
		return userJoinTimes;
	}

	public void setUserJoinTimes(int userJoinTimes) {
		this.userJoinTimes = userJoinTimes;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean getEveryWeek() {
		return everyWeek;
	}

	public void setEveryWeek(Boolean everyWeek) {
		this.everyWeek = everyWeek;
	}

	public List<LotteryGoodsForm> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<LotteryGoodsForm> goodsList) {
		this.goodsList = goodsList;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getUserAwardsTimes() {
		return userAwardsTimes;
	}

	public void setUserAwardsTimes(Integer userAwardsTimes) {
		this.userAwardsTimes = userAwardsTimes;
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

	public CoinsLotteryType getType() {
		return type;
	}

	public void setType(CoinsLotteryType type) {
		this.type = type;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getPhase1Start() {
		return phase1Start;
	}

	public void setPhase1Start(String phase1Start) {
		this.phase1Start = phase1Start;
	}

	public String getPhase1End() {
		return phase1End;
	}

	public void setPhase1End(String phase1End) {
		this.phase1End = phase1End;
	}

	public String getPhase2Start() {
		return phase2Start;
	}

	public void setPhase2Start(String phase2Start) {
		this.phase2Start = phase2Start;
	}

	public String getPhase2End() {
		return phase2End;
	}

	public void setPhase2End(String phase2End) {
		this.phase2End = phase2End;
	}

	public String getPhase3Start() {
		return phase3Start;
	}

	public void setPhase3Start(String phase3Start) {
		this.phase3Start = phase3Start;
	}

	public String getPhase3End() {
		return phase3End;
	}

	public void setPhase3End(String phase3End) {
		this.phase3End = phase3End;
	}

	public String getPhase4Start() {
		return phase4Start;
	}

	public void setPhase4Start(String phase4Start) {
		this.phase4Start = phase4Start;
	}

	public String getPhase4End() {
		return phase4End;
	}

	public void setPhase4End(String phase4End) {
		this.phase4End = phase4End;
	}

}
