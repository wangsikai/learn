package com.lanking.uxb.rescon.error.value;

import java.io.Serializable;
import java.util.Date;

/**
 * 纠错人VO
 * 
 * @author wangsenhao
 *
 */
public class VErrorUser implements Serializable {

	private static final long serialVersionUID = 4530967007670045929L;
	/**
	 * 纠错人
	 */
	private String userName;
	/**
	 * 纠错时间
	 */
	private Date errorTime;
	/**
	 * 纠错原因
	 */
	private String errorReason;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(Date errorTime) {
		this.errorTime = errorTime;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

}
