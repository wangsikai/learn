package com.lanking.uxb.zycon.operation.api;

import java.util.List;

import com.lanking.cloud.domain.support.console.common.CorrectUser;
import com.lanking.cloud.sdk.bean.Status;

/**
 * U 数学短信管理 相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月14日 下午5:24:22
 */
public interface ZycCorrectUserService {
	/**
	 * 新增用户
	 * 
	 * @param name
	 *            姓名
	 * @param tel
	 *            电话
	 * @return
	 */
	CorrectUser add(String name, String tel);

	/**
	 * 用户相关状态操作
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	CorrectUser edit(Long id, Status status);

	/**
	 * 所有用户 （删除除外）
	 * 
	 * @param index
	 * @return
	 */
	List<CorrectUser> list();

}
