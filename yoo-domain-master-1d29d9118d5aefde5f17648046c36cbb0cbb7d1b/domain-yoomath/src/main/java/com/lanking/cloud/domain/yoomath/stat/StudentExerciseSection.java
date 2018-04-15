package com.lanking.cloud.domain.yoomath.stat;

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
 * 按章节练习记录
 * 做过的题目：学生在悠数学中做过的题目的总数（对于没有批改结果的作业，不计入统计）。含作业中的、错题练习的以及自主练习的题目。如果同一题做了2次，计数为2
 * 做错的题目：学生在做过的题目中，做错的题目的数量。如果一道题，错了2次，计数为1.如果做错之后，又做对了，那么从做错的题目中清掉，即计数-1
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_exercise_section")
public class StudentExerciseSection implements Serializable {

	private static final long serialVersionUID = -734806877961836265L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 章节代码
	 */
	@Column(name = "section_code")
	private long sectionCode;

	/**
	 * 答题数量
	 */
	@Column(name = "do_count")
	private long doCount;

	/**
	 * 答错数量
	 */
	@Column(name = "wrong_count")
	private long wrongCount;

	/**
	 * 上个月答题数量
	 */
	@Column(name = "last_month_do_count", columnDefinition = "bigint default 0")
	private long lastMonthDoCount = 0;

	/**
	 * 上个月答错数量
	 */
	@Column(name = "last_month_wrong_count", columnDefinition = "bigint default 0")
	private long lastMonthWrongCount = 0;

	/**
	 * 上个月,如:201601
	 */
	@Column(name = "last_month", columnDefinition = "bigint default 0")
	private long lastMonth = 0;

	/**
	 * 当月答题数量
	 */
	@Column(name = "cur_month_do_count", columnDefinition = "bigint default 0")
	private long curMonthDoCount = 0;

	/**
	 * 当月答错数量
	 */
	@Column(name = "cur_month_wrong_count", columnDefinition = "bigint default 0")
	private long curMonthWrongCount = 0;

	/**
	 * 当月,如:201602
	 */
	@Column(name = "cur_month", columnDefinition = "bigint default 0")
	private long curMonth = 0;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public long getDoCount() {
		return doCount;
	}

	public void setDoCount(long doCount) {
		this.doCount = doCount;
	}

	public long getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(long wrongCount) {
		this.wrongCount = wrongCount;
	}

	public long getLastMonthDoCount() {
		return lastMonthDoCount;
	}

	public void setLastMonthDoCount(long lastMonthDoCount) {
		this.lastMonthDoCount = lastMonthDoCount;
	}

	public long getLastMonthWrongCount() {
		return lastMonthWrongCount;
	}

	public void setLastMonthWrongCount(long lastMonthWrongCount) {
		this.lastMonthWrongCount = lastMonthWrongCount;
	}

	public long getLastMonth() {
		return lastMonth;
	}

	public void setLastMonth(long lastMonth) {
		this.lastMonth = lastMonth;
	}

	public long getCurMonthDoCount() {
		return curMonthDoCount;
	}

	public void setCurMonthDoCount(long curMonthDoCount) {
		this.curMonthDoCount = curMonthDoCount;
	}

	public long getCurMonthWrongCount() {
		return curMonthWrongCount;
	}

	public void setCurMonthWrongCount(long curMonthWrongCount) {
		this.curMonthWrongCount = curMonthWrongCount;
	}

	public long getCurMonth() {
		return curMonth;
	}

	public void setCurMonth(long curMonth) {
		this.curMonth = curMonth;
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
