package com.lanking.uxb.service.thirdparty.api;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.thirdparty.ShareLog;
import com.lanking.uxb.service.thirdparty.form.ShareForm;

/**
 * 分享记录相关接口
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月11日
 */
public interface ShareLogService {

	ShareLog log(ShareForm form);

	/**
	 * 临时处理学生端客户端不能发布情况,后续删除
	 * 
	 * @author wangsenhao
	 * @version 2017.9.1
	 * 
	 * @param form
	 * @return
	 */
	ShareLog log2(ShareForm form);

	ShareLog get(long id);

	boolean isShare(Biz biz, long bizId);

	/**
	 * 
	 * @param biz
	 * @param bizId
	 * @param userId
	 * @param p0
	 *            附属字段
	 * @return
	 */
	boolean isShare(Biz biz, long bizId, Long userId, String p0);
}
