package com.lanking.uxb.channelSales.channel.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.channel.ChannelFile;
import com.lanking.uxb.channelSales.channel.form.ChannelFileForm;

/**
 * 渠道资料接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月3日
 */
public interface CsChannelFileService {

	/**
	 * 保存渠道资料.
	 * 
	 * @param form
	 *            表单
	 * @param userId
	 *            保存用户
	 * @return
	 */
	ChannelFile save(ChannelFileForm form, long userId);

	/**
	 * 查询渠道资料.
	 * 
	 * @return
	 */
	List<ChannelFile> findAll();

	/**
	 * 删除渠道资料.
	 * 
	 * @param fid
	 *            渠道资料ID
	 */
	void deleteFile(long fid);
}
