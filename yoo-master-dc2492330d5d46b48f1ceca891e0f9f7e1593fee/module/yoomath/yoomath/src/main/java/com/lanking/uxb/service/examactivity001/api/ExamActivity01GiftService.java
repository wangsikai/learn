package com.lanking.uxb.service.examactivity001.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001UserQ;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001User;

/**
 * 期末考试活动礼包相关接口
 * 
 * @author qiuxue.jiang
 *
 * @version 2017年12月27日
 */
public interface ExamActivity01GiftService {
	
	
	ExamActivity001UserQ getGift(Long id);
	
	/**
	 * 获取当前学生的礼包总数
	 * 
	 * @param code
	 * @param userId
	 */
	Long getGiftCount(Long code,Long userId);
	
	/**
	 * 获取当前学生的礼包列表
	 * 
	 * @param code
	 * @param userId
	 */
	List<ExamActivity001UserQ> getGifts(Long code,Long userId);
	
	/**
	 * 保存当前学生的qq号
	 * 
	 * @param code
	 * @param userId
	 */
	void saveQQ(Long code,Long userId,String qq);
	
	/**
	 * 添加礼包
	 * 
	 * @param code
	 * @param userId
	 */
	void addGift(Long code,Long userId);
	
	/**
	 * 确认抽奖
	 * 
	 * @param giftId
	 */
	void confirmGift(Long giftId);
}
