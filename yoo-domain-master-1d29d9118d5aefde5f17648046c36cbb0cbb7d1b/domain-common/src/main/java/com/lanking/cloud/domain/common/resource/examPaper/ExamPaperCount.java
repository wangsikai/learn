package com.lanking.cloud.domain.common.resource.examPaper;

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
 * 试卷统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "exam_paper_count")
public class ExamPaperCount implements Serializable {

	private static final long serialVersionUID = -1924868651451342865L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 试卷ID
	 */
	@Column(name = "exam_paper_id")
	private Long examPaperId;

	/**
	 * 定义几天的统计(比如最近7day30day,当为0时为全部天数)
	 */
	@Column(name = "n_day", precision = 5)
	private int nDay;

	/**
	 * n天中的第几天
	 */
	@Column(name = "day_of_n", precision = 5)
	private int dayOfN;

	/**
	 * 记录点击次数
	 */
	@Column(name = "click_count")
	private long clickCount;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "date")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExamPaperId() {
		return examPaperId;
	}

	public void setExamPaperId(Long examPaperId) {
		this.examPaperId = examPaperId;
	}

	public int getnDay() {
		return nDay;
	}

	public void setnDay(int nDay) {
		this.nDay = nDay;
	}

	public int getDayOfN() {
		return dayOfN;
	}

	public void setDayOfN(int dayOfN) {
		this.dayOfN = dayOfN;
	}

	public long getClickCount() {
		return clickCount;
	}

	public void setClickCount(long clickCount) {
		this.clickCount = clickCount;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
