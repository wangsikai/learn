package com.lanking.uxb.rescon.teach.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.uxb.rescon.teach.form.TeachAssistForm;

/**
 * 教辅版本操作接口.
 * 
 * @author
 * @since v1.3
 */
public interface ResconTeachAssistVersionManage {
	/**
	 * 
	 * @param id
	 * @return
	 */
	TeachAssistVersion get(Long id);

	/**
	 * 更新教辅状态
	 * 
	 * @param id
	 * @param status
	 */
	void updateTeachStatus(Long id, TeachAssistStatus status, VendorUser user);

	/**
	 * 获取教辅对应的一个或两个教辅版本
	 * 
	 * @param teachId
	 * @return
	 */
	List<TeachAssistVersion> listTeachVersion(Long teachId);

	/**
	 * 保存教辅版本
	 *
	 * @param form
	 *            {@link TeachAssistForm}
	 * @param updator
	 *            {@link VendorUser}
	 * @return {@link TeachAssistVersion}
	 */
	TeachAssistVersion save(TeachAssistForm form, VendorUser updator);

	/**
	 * 更新版本是否主版本
	 * 
	 * @param versionId
	 */
	void updateMainFlag(Long versionId, boolean mainFlag, Long teachAssistId);

	/**
	 * 获取当前教辅最大的版本号
	 * 
	 * @param teachAssistId
	 * @return
	 */
	Integer getMaxVersion(Long teachAssistId);

	/**
	 * 
	 * @param vendorId
	 * @return
	 */
	Map<Integer, Long> getStat(Long vendorId);

	/**
	 * 更新教辅版本封面
	 *
	 * @param versionId
	 *            版本id
	 * @param coverId
	 *            封面id
	 */
	void updateCover(long versionId, long coverId);

}
