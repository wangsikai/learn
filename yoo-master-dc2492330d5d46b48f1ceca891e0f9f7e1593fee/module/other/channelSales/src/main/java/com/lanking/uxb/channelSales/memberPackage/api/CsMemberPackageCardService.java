package com.lanking.uxb.channelSales.memberPackage.api;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageCardForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageCardQueryForm;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackageCard;

/**
 * 会员兑换卡管理
 *
 * @author zemin.song
 * @version 2016年11月15日
 */
public interface CsMemberPackageCardService {
	/**
	 * 批量生成卡号.
	 * 
	 * @param cards
	 *            会员卡
	 */
	void generateCodes(List<MemberPackageCard> cards);

	/**
	 * 会员卡查询
	 * 
	 * @param
	 * @return
	 */
	Page<MemberPackageCard> query(MemberPackageCardQueryForm query, Pageable pageable);

	/**
	 * 会员卡创建
	 * 
	 * @param
	 * @return MemberPackageCard 返回一条数据
	 */
	MemberPackageCard create(MemberPackageCardForm form);

	/**
	 * 会员卡更新
	 * 
	 * @param
	 * @return
	 */
	void update(MemberPackageCardForm form);

	/**
	 * 会员卡导出
	 * 
	 * @param
	 * @return
	 */
	HSSFWorkbook exportCards(List<VMemberPackageCard> cards);

	/**
	 * 查询最新创建的卡
	 * 
	 * @param form
	 * @return
	 */
	List<MemberPackageCard> queryNewCreate(MemberPackageCardForm form);

	/**
	 * 查询所有的卡
	 * 
	 * @param form
	 * @return
	 */
	List<MemberPackageCard> queryAll(MemberPackageCardQueryForm form);

	/**
	 * 获取创建用户LIST
	 * 
	 * @return
	 */
	List<ConsoleUser> getCreateUsers();

}
