package com.lanking.uxb.zycon.operation.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.common.BannerStatus;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycBannerQuery;
import com.lanking.uxb.zycon.operation.api.ZycBannerService;
import com.lanking.uxb.zycon.operation.form.BannerForm;

@Transactional(readOnly = true)
@Service
public class ZycBannerServiceImpl implements ZycBannerService {

	@Autowired
	@Qualifier("BannerRepo")
	Repo<Banner, Long> bannerRepo;
	private static Logger logger = Logger.getLogger(ZycBannerServiceImpl.class);

	@Override
	public List<Banner> list() {
		return bannerRepo.find("$list").list();
	}

	@Transactional
	@Override
	public void save(BannerForm form) {
		Banner banner = null;
		try {
			if (form.getId() != null) {
				banner = bannerRepo.get(form.getId());
				banner.setCreateAt(new Date());
				banner.setCreateId(form.getUserId());
			} else {
				banner = new Banner();
				banner.setCreateAt(new Date());
				banner.setCreateId(form.getUserId());
			}
			banner.setImageId(form.getImageId());
			banner.setUrl(form.getUrl());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (form.getStartAt() != null) {
				banner.setStartAt(sdf.parse(form.getStartAt()));
				// 如果从永久变成
				banner.setStatus(BannerStatus.DRAFT);
			} else {
				// 变成永久了
				banner.setStartAt(null);
			}
			if (form.getSequence() != null) {
				banner.setSequence(form.getSequence());
			}
			if (form.getEndAt() != null) {
				banner.setEndAt(sdf.parse(form.getEndAt()));
			} else {
				banner.setEndAt(null);
			}
			if (form.getStatus() != null) {
				banner.setStatus(form.getStatus());
			}
			if (form.getStartAt() == null && form.getEndAt() == null) {
				banner.setStatus(BannerStatus.PUBLISH);
			}
			if (form.getRealStatus() != null) {
				if (!form.getRealStatus().equals(this.getStatus(banner))) {
					// 说明当前状态发生了改变，需要置空序号
					banner.setSequence(null);
				}
			}
			banner.setApp(form.getApp());
			banner.setLocation(form.getLocation());
			bannerRepo.save(banner);
		} catch (Exception e) {
			logger.error("banner save error", e);
		}

	}

	public String getStatus(Banner s) {
		if (s.getStatus() == BannerStatus.PUBLISH) {
			return "已上架";
		} else if (s.getStatus() == BannerStatus.UN_PUBLISH) {
			return "已下架";
		} else if (s.getStatus() == BannerStatus.DRAFT) {
			if (s.getEndAt() != null) {
				if (s.getStartAt().getTime() < new Date().getTime() && s.getEndAt().getTime() > new Date().getTime()) {
					return "已上架";
				} else if (s.getEndAt().getTime() < new Date().getTime()) {
					return "已下架";
				} else {
					return "待上架";
				}
			} else {
				if (s.getStartAt().getTime() < new Date().getTime()) {
					return "已上架";
				} else {
					return "待上架";
				}
			}
		}
		return null;
	}

	@Transactional
	@Override
	public void delete() {
		bannerRepo.execute("$delBanner");
	}

	@Transactional
	@Override
	public void saveBanner(List<BannerForm> formList, Long userId) {
		this.delete();
		for (BannerForm form : formList) {
			form.setUserId(userId);
			save(form);
		}

	}

	@Transactional
	@Override
	public void updateStatus(Long id, BannerStatus status, Long updateId) {
		Banner banner = bannerRepo.get(id);
		banner.setStatus(status);
		banner.setUpdateId(updateId);
		banner.setUpdateAt(new Date());
		// 下架把之前的排列序号清空,以防止再次上架数据错误
		if (status == status.UN_PUBLISH) {
			banner.setSequence(null);
		}
		bannerRepo.save(banner);
	}

	@Override
	public List<Banner> queryList(ZycBannerQuery query) {
		Params params = Params.param();
		if (query.getStatus() != null) {
			params.put("status", query.getStatus().getValue());
		}
		if (query.getApp() != null) {
			params.put("app", query.getApp().getValue());
		}
		if (query.getLocation() != null) {
			params.put("location", query.getLocation().getValue());
		}
		return bannerRepo.find("$queryBannerList", params).list();
	}

	@Transactional
	@Override
	public void sortSquence(List<Long> ids) {
		int index = 1;
		for (Long id : ids) {
			Banner banner = bannerRepo.get(id);
			banner.setSequence(index);
			if (banner.getStatus() != BannerStatus.PUBLISH) {
				banner.setStatus(BannerStatus.PUBLISH);
			}
			bannerRepo.save(banner);
			index++;
		}
	}

	@Override
	public Banner get(Long id) {
		return bannerRepo.get(id);
	}

	@Override
	public List<Banner> bannerListByLocationAndApp(BannerLocation location, YooApp app) {
		Params params = Params.param();
		params.put("location", location.getValue());
		if (app != null) {
			params.put("app", app.getValue());
		}
		return bannerRepo.find("bannerCount", params).list();
	}
}
