package com.lanking.cloud.domain.yoomath.dailyPractise;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 章节&课时关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class DailyPractisePeriodKey implements Serializable {

	private static final long serialVersionUID = 6580799079826856042L;

	/**
	 * 章节代码
	 */
	@Id
	@Column(name = "section_code", nullable = false)
	private long sectionCode;

	/**
	 * 课时数
	 */
	@Id
	@Column(name = "period")
	private int period;

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
}
