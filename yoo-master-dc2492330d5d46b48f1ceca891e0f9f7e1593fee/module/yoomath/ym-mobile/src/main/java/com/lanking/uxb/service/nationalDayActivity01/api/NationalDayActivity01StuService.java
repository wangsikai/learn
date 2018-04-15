package com.lanking.uxb.service.nationalDayActivity01.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Stu;

/**
 * 国庆活动学生参与情况接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月26日
 */
public interface NationalDayActivity01StuService {

	List<NationalDayActivity01Stu> getTopN(int topn);

	/**
	 * 查询指定用户的参与信息
	 * 
	 * @param userId
	 */
	Map<String, Object> getStuByUser(long userId);
}
