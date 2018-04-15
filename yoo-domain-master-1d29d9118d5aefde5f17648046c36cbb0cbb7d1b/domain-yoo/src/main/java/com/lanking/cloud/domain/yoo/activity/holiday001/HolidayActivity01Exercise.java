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
 * 假期活动01-习题
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
@Table(name = "holiday_activity_01_exercise")
public class HolidayActivity01Exercise implements Serializable {

	private static final long serialVersionUID = -5618964261809551406L;

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
	 * 习题类型
	 */
	@Column(name = "type", precision = 3)
	private HolidayActivity01ExerciseType type;

	/**
	 * 习题名称
	 */
	@Column(name = "name", length = 128)
	private String name;

	/**
	 * 包含题目数量
	 */
	@Column(name = "question_count", precision = 3)
	private int questionCount;

	/**
	 * 版本代码
	 * 
	 * <pre>
	 * type = HolidayActivity01ExerciseType.PRESET时有效
	 * </pre>
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 年级
	 * 
	 * <pre>
	 * type = HolidayActivity01ExerciseType.PRESET的有效
	 * </pre>
	 */
	@Column(name = "grade", precision = 3)
	private HolidayActivity01Grade grade;

	/**
	 * 用户ID
	 * 
	 * <pre>
	 * type = HolidayActivity01ExerciseType.FALLIBLE | HolidayActivity01ExerciseType.KNOWLEDGE_POINT时有效
	 * </pre>
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 同一用户，同一类型下的练习编号（从1开始）
	 */
	@Column(name = "sequence", precision = 3)
	private int sequence;

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

	public HolidayActivity01ExerciseType getType() {
		return type;
	}

	public void setType(HolidayActivity01ExerciseType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public HolidayActivity01Grade getGrade() {
		return grade;
	}

	public void setGrade(HolidayActivity01Grade grade) {
		this.grade = grade;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}
