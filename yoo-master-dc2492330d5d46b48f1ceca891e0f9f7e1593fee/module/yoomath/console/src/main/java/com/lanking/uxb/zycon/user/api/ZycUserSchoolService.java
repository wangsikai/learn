package com.lanking.uxb.zycon.user.api;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.user.form.SchoolForm;

/**
 * 学校管理接口
 * 
 * @author wangsenhao
 *
 */
public interface ZycUserSchoolService {
	/**
	 * 添加或编辑学校
	 * 
	 * @param form
	 */
	void saveSchool(SchoolForm form);

	/**
	 * 查询学校
	 * 
	 * @param form
	 * @return
	 */
	Page<School> query(SchoolForm query, Pageable p);

	School getSchool(Long id);

	/**
	 * 将修改后的学校应用到系统，更新到缓存
	 */
	void syncData();

}
