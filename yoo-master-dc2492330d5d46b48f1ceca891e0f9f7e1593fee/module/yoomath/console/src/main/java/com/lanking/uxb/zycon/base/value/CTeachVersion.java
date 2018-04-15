package com.lanking.uxb.zycon.base.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 教材版本VO
 * 
 * @author wangsenhao
 * @since 2.1
 *
 */
public class CTeachVersion implements Serializable {

	private static final long serialVersionUID = 8248842532850154355L;
	/**
	 * 版本编号
	 */
	private Integer vcode;
	/**
	 * 版本名称
	 */
	private String vname;
	/**
	 * 版本顺序
	 */
	private Integer vsequence;
	/**
	 * 教材编号
	 */
	private Integer tcode;
	/**
	 * 教材名称
	 */
	private String tname;
	/**
	 * 教材顺序
	 */
	private Integer tsequence;
	/**
	 * 版本悠数学状态
	 */
	private Status vStatus;
	/**
	 * 教材悠数学状态
	 */
	private Status tStatus;
	/**
	 * 高中初中会出现同样的版本，用于区分
	 */
	private String phaseName;

	public Status getvStatus() {
		return vStatus;
	}

	public void setvStatus(Status vStatus) {
		this.vStatus = vStatus;
	}

	public Status gettStatus() {
		return tStatus;
	}

	public void settStatus(Status tStatus) {
		this.tStatus = tStatus;
	}

	public Integer getVcode() {
		return vcode;
	}

	public void setVcode(Integer vcode) {
		this.vcode = vcode;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public Integer getVsequence() {
		return vsequence;
	}

	public void setVsequence(Integer vsequence) {
		this.vsequence = vsequence;
	}

	public Integer getTcode() {
		return tcode;
	}

	public void setTcode(Integer tcode) {
		this.tcode = tcode;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Integer getTsequence() {
		return tsequence;
	}

	public void setTsequence(Integer tsequence) {
		this.tsequence = tsequence;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

}
