/**
 * 
 */
package com.lanking.uxb.zycon.activity.api;

import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * 科举活动用户接口
 * 
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
public interface ZycImperialExaminationActivityUserService {

	/**
	 * 科举活动报名用户
	 * 
	 * @param activityCode
	 *            活动code
	 */
	Page<Map> findActivityUser(ZycActivityUserForm form, Pageable p);

}
