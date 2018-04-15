package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.base.session.SessionHistory;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.operation.form.UserSearchForm;

/**
 * U数学管控台 session histroy 接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月14日 上午11:30:44
 */
public interface ZycSessionHistoryService {

	/**
	 * 根据查询条件获取session history
	 * 
	 * @param searchForm
	 *            查询条件
	 * @param pageable
	 *            分页
	 * @return
	 */
	Page<SessionHistory> getHistoryUsers(UserSearchForm searchForm, Pageable pageable);
}
