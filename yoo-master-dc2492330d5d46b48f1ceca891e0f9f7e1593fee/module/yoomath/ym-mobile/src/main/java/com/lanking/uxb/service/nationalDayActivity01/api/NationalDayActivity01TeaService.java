package com.lanking.uxb.service.nationalDayActivity01.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;

/**
 * 国庆活动教师参与情况接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月25日
 */
public interface NationalDayActivity01TeaService {

	/**
	 * 查询排行榜前x名
	 * 
	 * @param count
	 * @return list
	 */
	List<NationalDayActivity01Tea> getTopN(int count);

	/**
	 * 查询指定用户的参与信息
	 * 
	 * @param userId
	 */
	Map<String, Object> getTeaByUser(long userId);
}
