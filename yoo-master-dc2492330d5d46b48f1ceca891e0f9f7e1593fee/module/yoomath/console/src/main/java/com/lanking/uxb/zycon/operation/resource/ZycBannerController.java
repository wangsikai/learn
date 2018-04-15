package com.lanking.uxb.zycon.operation.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerStatus;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.operation.api.ZycBannerQuery;
import com.lanking.uxb.zycon.operation.api.ZycBannerService;
import com.lanking.uxb.zycon.operation.convert.ZycBannerConvert;
import com.lanking.uxb.zycon.operation.form.BannerForm;
import com.lanking.uxb.zycon.operation.value.VZycBanner;

/**
 * 悠数学广告位后台管理
 * 
 * @since 2.0
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/banner")
public class ZycBannerController {

	@Autowired
	private ZycBannerService zycBannerService;
	@Autowired
	private ZycBannerConvert zycBannerConvert;

	/**
	 * 获取广告位列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list")
	public Value list() {
		List<Banner> list = zycBannerService.list();
		return new Value(zycBannerConvert.to(list));
	}

	/**
	 * 保存广告位
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "saveBanner")
	public Value saveBanner(@RequestParam(value = "list") String json) {
		List<BannerForm> jsonlist = JSON.parseArray(json, BannerForm.class);
		zycBannerService.saveBanner(jsonlist, Security.getUserId());
		return new Value();
	}

	/**
	 * 查询
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "queryList")
	public Value queryList(ZycBannerQuery query) {
		List<Banner> list = zycBannerService.queryList(query);
		List<VZycBanner> vlist = zycBannerConvert.to(list);
		// 特殊处理通过设置时间，上架和下架的banner
		List<VZycBanner> list1 = new ArrayList<VZycBanner>();
		List<VZycBanner> list2 = new ArrayList<VZycBanner>();
		List<VZycBanner> list3 = new ArrayList<VZycBanner>();
		for (VZycBanner v : vlist) {
			if (v.getRealStatus() == "已上架") {
				list1.add(v);
			}
			if (v.getRealStatus() == "待上架") {
				list2.add(v);
			}
			if (v.getRealStatus() == "已下架") {
				list3.add(v);
			}
		}
		list1.addAll(list2);
		list1.addAll(list3);
		return new Value(list1);
	}

	/**
	 * 获取banner对象
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getBannerById")
	public Value getBannerById(Long id) {
		Banner banner = zycBannerService.get(id);
		return new Value(zycBannerConvert.to(banner));
	}

	/**
	 * 保存单个广告位
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "saveOneBanner")
	public Value saveOneBanner(BannerForm form) {
		// 只有增加的时候判断
		if (form.isNeedCheckCount() || !form.getRealStatus().equals(this.getStatus(form))) {
			List<Banner> list = zycBannerService.bannerListByLocationAndApp(form.getLocation(), form.getApp());
			List<VZycBanner> vlist = zycBannerConvert.to(list);
			int count = 0;
			for (VZycBanner v : vlist) {
				if (v.getRealStatus() == "已上架" || v.getRealStatus() == "待上架") {
					count++;
				}
			}
			if (count >= 8) {
				return new Value(count);
			}
		}
		zycBannerService.save(form);
		return new Value();
	}

	public String getStatus(BannerForm s) {
		try {
			if (s.getStartAt() == null && s.getEndAt() == null) {
				return "已上架";
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startAt = sdf.parse(s.getStartAt());
				Date endAt = sdf.parse(s.getStartAt());
				if (s.getEndAt() != null) {
					if (endAt != null) {
						if (startAt.getTime() < new Date().getTime() && endAt.getTime() > new Date().getTime()) {
							return "已上架";
						} else if (endAt.getTime() < new Date().getTime()) {
							return "已下架";
						} else {
							return "待上架";
						}
					} else {
						if (startAt.getTime() < new Date().getTime()) {
							return "已上架";
						} else {
							return "待上架";
						}
					}
				}
			}
		} catch (Exception e) {

		}

		return null;
	}

	/**
	 * 上移或者下移界面保存
	 * 
	 * @param upId
	 *            上移的ID
	 * @param downId
	 *            下移的ID
	 * @return
	 */
	@RequestMapping(value = "moveAndSave")
	public Value moveAndSave(BannerForm form) {
		zycBannerService.sortSquence(form.getIds());
		return new Value();
	}

	/**
	 * 删除banner
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delBanner")
	public Value delBanner(Long id) {
		zycBannerService.updateStatus(id, BannerStatus.DELETE, Security.getUserId());
		return new Value();
	}

	/**
	 * 更新状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "updateStatus")
	public Value updateStatus(Long id, BannerStatus status) {
		zycBannerService.updateStatus(id, status, Security.getUserId());
		return new Value();
	}
}
