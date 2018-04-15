package com.lanking.uxb.rescon.teach.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssist;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.teach.form.TeachAssistForm;

/**
 * 教辅操作接口.
 * 
 * @author wlche
 *
 */
public interface ResconTeachAssistManage {

	/**
	 * 创建教辅（非编辑）.
	 * 
	 * @param form
	 *            参数
	 * @param creater
	 *            操作人
	 * @param 教辅
	 */
	TeachAssist createTeachAssist(TeachAssistForm form, VendorUser creater) throws ResourceConsoleException;

	/**
	 * 批量查询教辅版本
	 * 
	 * @param ids
	 *            教辅ID
	 * @return
	 */
	List<TeachAssistVersion> listTeachAssistVersion(Collection<Long> ids);

	/**
	 * 批量查询教辅
	 * 
	 * @param ids
	 *            教辅ID .
	 * @return
	 */
	Map<Long, TeachAssist> mgetTeachAssist(Collection<Long> ids);

}
