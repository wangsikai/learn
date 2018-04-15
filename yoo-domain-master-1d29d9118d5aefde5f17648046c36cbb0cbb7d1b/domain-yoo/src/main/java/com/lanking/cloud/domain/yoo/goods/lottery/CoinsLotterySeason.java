package com.lanking.cloud.domain.yoo.goods.lottery;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 抽奖期别
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_lo_season")
public class CoinsLotterySeason implements Serializable {

	private static final long serialVersionUID = -5008057873318253732L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 抽奖code,同一抽奖不同期code一致
	 */
	@Column(name = "code")
	private Integer code;

	/**
	 * 抽奖类型
	 */
	@Column(name = "type", precision = 3)
	private CoinsLotteryType type;

	/**
	 * 开始时间
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@Column(name = "end_time", columnDefinition = "datetime(3)")
	private Date endTime;

	/**
	 * 一个用户每天可以参加的次数
	 */
	@Column(name = "user_join_times", precision = 3)
	private int userJoinTimes;

	/**
	 * 每人每天的中奖次数限制,-1表示无此设置
	 */
	@Column(name = "user_awards_times", precision = 3)
	private Integer userAwardsTimes;

	/**
	 * 奖池每天可以中奖的次数限制,-1表示无此设置
	 */
	@Column(name = "awards_times")
	private Integer awardsTimes;

	/**
	 * 某个用户${mustAwardsTimes-1}次没有中奖,第${mustAwardsTimes}必定中奖,-1表示无此设置
	 */
	@Column(name = "must_awards_times")
	private Integer mustAwardsTimes;

	/**
	 * 对应mustAwardsTimes的奖品坑位
	 */
	@Column(name = "must_awards_goods", precision = 3)
	private Integer mustAwardsGoods;

	/**
	 * 标题
	 */
	@Column(name = "title", length = 40)
	private String title;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	/**
	 * 是不是每周一次
	 */
	@Column(name = "every_week", precision = 2)
	private Boolean everyWeek = true;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 每期净收入金币数量
	 */
	@Column(name = "earn_coins")
	private Integer earnCoins;

	/**
	 * 面向用户,0时表示学生和老师
	 */
	@Column(name = "user_type", precision = 3)
	private UserType userType;

	/**
	 * 抽奖活动名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public CoinsLotteryType getType() {
		return type;
	}

	public void setType(CoinsLotteryType type) {
		this.type = type;
	}

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

	public Boolean getEveryWeek() {
		return everyWeek;
	}

	public void setEveryWeek(Boolean everyWeek) {
		this.everyWeek = everyWeek;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getEarnCoins() {
		return earnCoins;
	}

	public void setEarnCoins(Integer earnCoins) {
		this.earnCoins = earnCoins;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
