package com.lanking.uxb.service.zuoye.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 学校书本相关接口
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月23日 下午3:21:23
 */
public interface ZySchoolBookService {

	/**
	 * 根据状态 获取对应学校的学校书本数量
	 * 
	 * @param schoolId
	 *            学校Id
	 * @param status
	 *            状态
	 * @return
	 */
	Integer getSchoolBookByStatus(long schoolId, Status status);

	/**
	 * 获取userSchoolBookMap
	 * 
	 * @param unionIds
	 *            是接口外构造的schoolId 和 bookId 拼成的 Long型数据（如schoolId 为1，bookId
	 *            为2，unionId为12。）list
	 * @return key bookId，value 为schooBookId
	 */
	Map<Long, Long> findBySchoolAndBooK(List<String> unionIds);

}
