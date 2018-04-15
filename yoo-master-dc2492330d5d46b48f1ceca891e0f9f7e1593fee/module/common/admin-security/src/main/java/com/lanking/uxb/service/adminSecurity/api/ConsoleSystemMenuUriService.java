package com.lanking.uxb.service.adminSecurity.api;

import java.util.List;

import com.lanking.cloud.domain.support.common.auth.ConsoleSystemMenuUri;

/**
 * 系统对应菜单地址代码
 * 
 * @author wangsenhao
 *
 */
public interface ConsoleSystemMenuUriService {

	/**
	 * 通过系统过滤菜单
	 * 
	 * @param systemId
	 *            系统id
	 * @return
	 */
	List<ConsoleSystemMenuUri> queryBySystem(long systemId);

	/**
	 * 增加数据
	 * 
	 * @param systemId
	 * @param menuUri
	 * @param description
	 */
	void addUri(long systemId, String menuUri, String description);

	/**
	 * 修改数据
	 * 
	 * @param id
	 * @param systemId
	 * @param menuUri
	 * @param description
	 */
	void updateUri(long id, Long systemId, String menuUri, String description);
}
