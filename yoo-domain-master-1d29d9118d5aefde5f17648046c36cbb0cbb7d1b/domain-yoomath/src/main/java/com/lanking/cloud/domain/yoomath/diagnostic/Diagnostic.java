package com.lanking.cloud.domain.yoomath.diagnostic;

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
 * 诊断-教材维度的全局做题统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "diagno")
public class Diagnostic implements Serializable {

	private static final long serialVersionUID = 1487544256600815465L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private int textbookCode;

	/**
	 * 月答题数量
	 */
	@Column(name = "class_month_do_count", precision = 7)
	private int classMonthDoCount;

	/**
	 * 答对的数量
	 */
	@Column(name = "class_month_right_count", precision = 7)
	private int classMonthRightCount;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(int textbookCode) {
		this.textbookCode = textbookCode;
	}

	public int getClassMonthDoCount() {
		return classMonthDoCount;
	}

	public void setClassMonthDoCount(int classMonthDoCount) {
		this.classMonthDoCount = classMonthDoCount;
	}

	public int getClassMonthRightCount() {
		return classMonthRightCount;
	}

	public void setClassMonthRightCount(int classMonthRightCount) {
		this.classMonthRightCount = classMonthRightCount;
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
