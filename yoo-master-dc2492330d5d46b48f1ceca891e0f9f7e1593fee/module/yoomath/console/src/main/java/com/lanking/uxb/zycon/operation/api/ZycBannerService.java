package com.lanking.uxb.zycon.operation.api;

import java.util.List;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.common.BannerStatus;
import com.lanking.uxb.zycon.operation.form.BannerForm;

/**
 * 悠数学广告位
 * 
 * @since 2.0
 * @author wangsenhao
 *
 */
public interface ZycBannerService {
	/**
	 * 广告位列表，最多五个
	 * 
	 * @return
	 */
	List<Banner> list();

	/**
	 * 保存广告位
	 * 
	 * @param form
	 */
	void save(BannerForm form);

	/**
	 * 删除广告位
	 */
	void delete();

	void saveBanner(List<BannerForm> formList, Long userId);

	/**
	 * 更新状态
	 * 
	 * @param id
	 * @param status
	 */
	void updateStatus(Long id, BannerStatus status, Long updateId);

	List<Banner> queryList(ZycBannerQuery query);

	/**
	 * 排序
	 * 
	 * @param upId
	 * @param downId
	 */
	void sortSquence(List<Long> ids);

	Banner get(Long id);

	/**
	 * 获取待上架和已上架的banner
	 * 
	 * @param location
	 *            banner位置
	 * @param app
	 *            banner是属于客户端还是web端
	 * @return
	 */
	List<Banner> bannerListByLocationAndApp(BannerLocation location, YooApp app);
}
