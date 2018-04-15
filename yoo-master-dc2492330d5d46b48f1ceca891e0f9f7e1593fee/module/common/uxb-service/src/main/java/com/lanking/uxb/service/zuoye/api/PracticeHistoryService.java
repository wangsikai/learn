package com.lanking.uxb.service.zuoye.api;

import java.util.Date;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.common.PracticeHistory;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.zuoye.form.PracticeHistoryForm;

/**
 * 练习历史相关接口
 * 
 * @since yoomath(mobile) V1.1.0
 * @author wangsenhao
 *
 */
public interface PracticeHistoryService {
	/**
	 * 更新或者新增历史列表
	 * 
	 * @param form
	 */
	void updateHistory(PracticeHistoryForm form);

	/**
	 * 查询联系历史
	 * 
	 * @param type
	 *            1:章节练习2:每日练3:智能试卷<br>
	 *            不传表示查询全部，不过滤
	 */
	CursorPage<Long, PracticeHistory> queryHistory(Integer type, CursorPageable<Long> cpr, Date cursorDate, Long userId);

	/**
	 * 通过系统业务KEY和业务ID获取练习历史对象
	 * 
	 * @param biz
	 *            系统业务key
	 * @param bizId
	 *            业务ID
	 * @return
	 */
	PracticeHistory getByBizId(Biz biz, long bizId);

	/**
	 * 获取某个用户练习历史数量
	 * 
	 * @param userId
	 *            用户ID
	 * @return 用户练习历史数量
	 */
	long count(long userId);

}
