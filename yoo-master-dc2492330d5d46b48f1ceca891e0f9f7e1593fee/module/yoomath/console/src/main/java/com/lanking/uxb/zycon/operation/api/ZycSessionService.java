package com.lanking.uxb.zycon.operation.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.base.session.Session;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.operation.form.UserSearchForm;

/**
 * U数学管控台session接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年9月22日 下午4:09:01
 */
public interface ZycSessionService {

	/**
	 * 根据查询条件获取session
	 * 
	 * searchForm 搜索条件form
	 * 
	 * @param pageable
	 *            分页
	 * @return
	 */
	Page<Session> getSessionByParams(UserSearchForm searchForm, Pageable pageable);

	/**
	 * 根据查询条件获取session
	 * 
	 * searchForm 搜索条件form
	 * 
	 * @param pageable
	 *            分页
	 * @return
	 */
	Page<Session> getSessionByUserId(List<Long> userIds, Pageable pageable, Date activeAtEnd);
}
