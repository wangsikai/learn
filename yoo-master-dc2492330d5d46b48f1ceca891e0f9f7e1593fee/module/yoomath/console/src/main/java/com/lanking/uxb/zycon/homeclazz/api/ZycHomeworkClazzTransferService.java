package com.lanking.uxb.zycon.homeclazz.api;

import java.util.Map;

public interface ZycHomeworkClazzTransferService {

	/**
	 * 转让班级,每次转让都会产生新的数据
	 * 
	 * @param classId
	 * @param to
	 *            转让给的老师
	 */
	void classTransfer(long classId, long to);

	/**
	 * 通过账户名查找信息，这里是精确匹配
	 * 
	 * @param accountName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map findUser(String accountName);
}
