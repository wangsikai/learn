package com.lanking.uxb.service.holidayActivity01.value;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户pk记录VO
 * 
 * @author peng.zhao
 * @version 2018年1月22日
 */
public class VHolidayActivity02PKRecord implements Serializable {

	private static final long serialVersionUID = 398165706660661914L;

	private Long id;
	
	/**
	 * 对战时间
	 */
	private Date pkAt;
	
	/**
	 * 对手用户名
	 */
	private String pkUserName;
	
	/**
	 * 对战结果
	 * 0:失败,1:平局,2:胜利
	 */
	private Integer pkResult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPkAt() {
		return pkAt;
	}

	public void setPkAt(Date pkAt) {
		this.pkAt = pkAt;
	}

	public String getPkUserName() {
		return pkUserName;
	}

	public void setPkUserName(String pkUserName) {
		this.pkUserName = pkUserName;
	}

	public Integer getPkResult() {
		return pkResult;
	}

	public void setPkResult(Integer pkResult) {
		this.pkResult = pkResult;
	}
}
