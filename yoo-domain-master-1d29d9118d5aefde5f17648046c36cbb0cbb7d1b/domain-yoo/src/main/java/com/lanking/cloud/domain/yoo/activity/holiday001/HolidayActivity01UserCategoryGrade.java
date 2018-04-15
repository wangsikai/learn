package com.lanking.cloud.domain.yoo.activity.holiday001;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 假期活动01(后面要是有活动和此活动相似度>90可以考虑复用，其他一律不建议复用)
 * 
 * <pre>
 * 1.活动名称：”这个暑假，有我，由你”(2017年6月13日)
 * </pre>
 * 
 * @since 4.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月13日
 */
@Entity
@Table(name = "holiday_activity_01_user_category_grade")
public class HolidayActivity01UserCategoryGrade implements Serializable {

	private static final long serialVersionUID = -3729798361688553567L;
	
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
	 * 用户ID(教师用户)
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 版本代码
	 * 
	 * <pre>
	 * </pre>
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 年级
	 * 
	 * <pre>
	 * </pre>
	 */
	@Column(name = "grade", precision = 3)
	private Integer grade;

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

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
}
