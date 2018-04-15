package com.lanking.cloud.domain.yoomath.dailyPractise;

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
 * 每日练参数设置
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "daily_practice_settings")
public class DailyPracticeSettings implements Serializable {

	private static final long serialVersionUID = -7096112607861229792L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 章节代码
	 */
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 难度
	 */
	@Column(name = "difficulty", precision = 3)
	private DailyPracticeDifficulty difficulty;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 此教材当前章节
	 */
	@Column(name = "cur_section_code")
	private Long curSectionCode;

	/**
	 * 此教材当前章节当前课时
	 */
	@Column(name = "cur_period")
	private Integer curPeriod;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public DailyPracticeDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(DailyPracticeDifficulty difficulty) {
		this.difficulty = difficulty;
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

	public Long getCurSectionCode() {
		return curSectionCode;
	}

	public void setCurSectionCode(Long curSectionCode) {
		this.curSectionCode = curSectionCode;
	}

	public Integer getCurPeriod() {
		return curPeriod;
	}

	public void setCurPeriod(Integer cur_period) {
		this.curPeriod = cur_period;
	}

}
