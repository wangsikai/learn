package com.lanking.uxb.service.diagnostic.value;

import com.lanking.uxb.service.user.value.VStudent;
import com.lanking.uxb.service.user.value.VUser;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * value for DiagnosticClassStudent
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VDiagnosticClassStudent implements Serializable {
	private static final long serialVersionUID = 1711033283342290648L;

	private long id;
	private Integer rank;
	private int floatRank;
	private BigDecimal rightRate;
	private String rightRateTitle;
	private int homeworkCount;
	private List<BigDecimal> rightRates;
	private VUser user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public int getFloatRank() {
		return floatRank;
	}

	public void setFloatRank(int floatRank) {
		this.floatRank = floatRank;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public int getHomeworkCount() {
		return homeworkCount;
	}

	public void setHomeworkCount(int homeworkCount) {
		this.homeworkCount = homeworkCount;
	}

	public List<BigDecimal> getRightRates() {
		return rightRates;
	}

	public void setRightRates(List<BigDecimal> rightRates) {
		this.rightRates = rightRates;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}
}
