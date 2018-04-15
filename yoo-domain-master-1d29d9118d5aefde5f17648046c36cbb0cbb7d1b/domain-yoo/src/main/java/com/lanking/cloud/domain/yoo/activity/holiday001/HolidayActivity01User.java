package com.lanking.cloud.domain.yoo.activity.holiday001;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 假期活动01-参与活动的用户
 * 
 * <pre>
 * 1.”这个暑假，有我，由你”(2017年6月13日):参与用户布置作业的时候写入,布置第一份作业的时候写入
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
@Entity
@Table(name = "holiday_activity_01_user")
public class HolidayActivity01User implements Serializable {

	private static final long serialVersionUID = -7753947102619933525L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 活动代码
	 */
	@Column(name = "activity_code")
	private long activityCode;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 总共拥有的抽奖机会次数（只增不减）
	 */
	@Column(name = "lucky_draw")
	private int luckyDraw;

	/**
	 * 花费的抽奖次数
	 */
	@Column(name = "cost_lucky_draw")
	private int costLuckyDraw;

	/**
	 * 新得的抽奖机会(用来提醒,提醒后需要更新为0)
	 */
	@Column(name = "new_lucky_draw")
	private int newLuckyDraw;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(long activityCode) {
		this.activityCode = activityCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getLuckyDraw() {
		return luckyDraw;
	}

	public void setLuckyDraw(int luckyDraw) {
		this.luckyDraw = luckyDraw;
	}

	public int getCostLuckyDraw() {
		return costLuckyDraw;
	}

	public void setCostLuckyDraw(int costLuckyDraw) {
		this.costLuckyDraw = costLuckyDraw;
	}

	public int getNewLuckyDraw() {
		return newLuckyDraw;
	}

	public void setNewLuckyDraw(int newLuckyDraw) {
		this.newLuckyDraw = newLuckyDraw;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
