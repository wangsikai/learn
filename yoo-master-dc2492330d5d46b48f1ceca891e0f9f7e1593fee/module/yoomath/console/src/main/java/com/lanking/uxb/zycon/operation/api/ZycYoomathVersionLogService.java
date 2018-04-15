package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.operation.form.VersionForm;

/**
 * 悠数学 版本更新 service 接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月9日 下午3:48:51
 */
public interface ZycYoomathVersionLogService {
	/**
	 * 新增版本日志
	 * 
	 * @param form
	 *            版本LOG
	 */
	YoomathVersionLog add(VersionForm form);

	/**
	 * 版本日志相关状态操作
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	YoomathVersionLog edit(Long id, Status status);

	/**
	 * versionLog query
	 * 
	 * @param index
	 * @return
	 */
	Page<YoomathVersionLog> query(Pageable index);

}
