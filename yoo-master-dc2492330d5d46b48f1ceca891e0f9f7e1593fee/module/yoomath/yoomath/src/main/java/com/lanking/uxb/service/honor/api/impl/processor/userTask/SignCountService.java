/**
 * 
 */
package com.lanking.uxb.service.honor.api.impl.processor.userTask;

import java.util.Map;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
public interface SignCountService {
	/**
	 * 累计签到成就任务
	 * 
	 * @param checkCode
	 * @param userId
	 */
	void sign(long checkCode, long userId, Map<String, Object> params);

}
