package com.lanking.uxb.service.imperial02.value;

import java.io.Serializable;
import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivityAward;

/**
 * 颁奖页数据集合.
 * 
 * @author peng.zhao
 *
 * @version 2017年11月13日
 */
public class VImperialExaminationAwardDatas2 implements Serializable {

	private static final long serialVersionUID = 6307260524515015807L;

	/**
	 * 一等奖.
	 */
	private List<VImperialExaminationActivityAward> awardLevel1 = Lists.newArrayList();

	/**
	 * 二等奖.
	 */
	private List<VImperialExaminationActivityAward> awardLevel2 = Lists.newArrayList();

	/**
	 * 三等奖.
	 */
	private List<VImperialExaminationActivityAward> awardLevel3 = Lists.newArrayList();
	/**
	 * 个人奖项.
	 */
	private VImperialExaminationActivityAward self;

	/**
	 * 用户报名时填写的姓名.
	 */
	private String name;

	/**
	 * 用户报名时填写的电话.
	 */
	private String mobile;

	public List<VImperialExaminationActivityAward> getAwardLevel1() {
		return awardLevel1;
	}

	public void setAwardLevel1(List<VImperialExaminationActivityAward> awardLevel1) {
		this.awardLevel1 = awardLevel1;
	}

	public List<VImperialExaminationActivityAward> getAwardLevel2() {
		return awardLevel2;
	}

	public void setAwardLevel2(List<VImperialExaminationActivityAward> awardLevel2) {
		this.awardLevel2 = awardLevel2;
	}

	public List<VImperialExaminationActivityAward> getAwardLevel3() {
		return awardLevel3;
	}

	public void setAwardLevel3(List<VImperialExaminationActivityAward> awardLevel3) {
		this.awardLevel3 = awardLevel3;
	}

	public VImperialExaminationActivityAward getSelf() {
		return self;
	}

	public void setSelf(VImperialExaminationActivityAward self) {
		this.self = self;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
