/**
 * 
 */
package com.lanking.uxb.zycon.parameter.api;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.parameter.form.ParameterForm;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
public interface ZycParameterService {

	/**
	 * 查询平台配置信息
	 *
	 * @param form
	 *            {@link ParameterForm}
	 * @return {Page}
	 */
	Page<Parameter> getAllList(Pageable p);

	/**
	 * 存储配置信息
	 *
	 * @param form
	 *            {@link ParameterForm}
	 * @return {Page}
	 */
	int save(ParameterForm form);

	/**
	 * 删除配置信息
	 *
	 * @param form
	 *            {@link ParameterForm}
	 * @return {Page}
	 */
	int dalete(Long id);

	/**
	 * 同步缓存数据
	 */
	void syncData();

}
