package com.lanking.uxb.service.zuoye.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;

/**
 * 悠数学 版本更新接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月10日 上午9:39:51
 */
public interface ZyYoomathVersionLogService {

	/**
	 * 最新N条悠数学版本更新记录（已发布的）
	 * 
	 * @return
	 */
	List<YoomathVersionLog> latestVersionLogs(Integer limit);

}
