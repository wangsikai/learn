package com.lanking.uxb.rescon.statistics.api;

import java.util.Date;

/**
 * 统计数据的相关输入接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月31日
 */
public interface VendorUserStatisManage {

	/**
	 * 首次保存为草稿后调用
	 * 
	 * @since V2.1
	 * @param builderId
	 *            录入员ID
	 */
	void updateAfterDraft(long builderId);

	/**
	 * 录入后调用
	 * 
	 * @since V2.1
	 * @param builderId
	 *            录入员ID
	 * @param fromDraft
	 *            是否从草稿保存
	 * @param questionCreateAt
	 *            原题目创建时间,fromDraft为true时此参数有效
	 */
	void updateAfterBuild(long builderId, boolean fromDraft, Date questionCreateAt);

	/**
	 * 审核不通过再次编辑后调用
	 * 
	 * @since V2.1
	 * @param builderId
	 *            录入员ID
	 * @param questionCreateAt
	 *            原题目创建时间
	 */
	void updateAfterNoPassBuild(long builderId, Date questionCreateAt);

	/**
	 * 已通过题目打回重新校验调用
	 * 
	 * @since V2.1
	 * @param builderId
	 *            录入员ID
	 * @param questionCreateAt
	 *            原题目创建时间
	 */
	void updateAfterPassRecheck(long builderId, Date questionCreateAt);

	/**
	 * 通过校验后调用(一校)
	 * 
	 * @since 2.1
	 * @param builderId
	 *            录入员ID
	 * @param checkerId
	 *            校验员ID
	 * @param edit
	 *            是否编辑
	 * @param questionCreateAt
	 *            原题目创建时间
	 */
	void updateAfterStep1Pass(long builderId, long checkerId, boolean edit, Date questionCreateAt);

	/**
	 * 未通过校验后调用(一校)
	 * 
	 * @since 2.1
	 * @param builderId
	 *            录入员ID
	 * @param checkerId
	 *            校验员ID
	 * @param questionCreateAt
	 *            原题目创建时间
	 */
	void updateAfterStep1NoPass(long builderId, long checkerId, Date questionCreateAt);

	/**
	 * 通过校验后调用
	 * 
	 * @since 2.1
	 * @param builderId
	 *            录入员ID
	 * @param checkerId
	 *            校验员ID
	 * @param edit
	 *            是否编辑
	 * @param questionCreateAt
	 *            原题目创建时间
	 */
	void updateAfterStep2Pass(long builderId, long checkerId, boolean edit, Date questionCreateAt);

	/**
	 * 未通过校验后调用
	 * 
	 * @since 2.1
	 * @param builderId
	 *            录入员ID
	 * @param checkerId
	 *            校验员ID
	 * @param questionCreateAt
	 *            原题目创建时间
	 */
	void updateAfterStep2NoPass(long builderId, long checkerId, Date questionCreateAt);

	/**
	 * 删除草稿后调用
	 * 
	 * @since V2.1
	 * @param builderId
	 *            录入员ID
	 */
	void deleteAfterDraft(long builderId);
}
