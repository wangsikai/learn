package com.lanking.uxb.rescon.book.api;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.School;

/**
 * 校本图书接口.
 * 
 * @author wlche
 * @since rescon v1.2.6
 */
public interface ResconSchoolBookManage {

	/**
	 * 获取学校列表.
	 * 
	 * @param bookid
	 *            书本ID
	 * @return
	 */
	List<School> listSchool(long bookid);
}
