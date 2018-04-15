package com.lanking.uxb.zycon.client.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.version.YoomathClientVersion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.client.api.ZycClientVersionService;
import com.lanking.uxb.zycon.client.form.CVersionForm;

@Transactional(readOnly = true)
@Service
public class ZycClientVersionServiceImpl implements ZycClientVersionService {

	@Autowired
	@Qualifier("YoomathClientVersionRepo")
	Repo<YoomathClientVersion, Long> yoomathClientVersionRepo;

	@Override
	public List<YoomathClientVersion> queryVersion(YooApp app, DeviceType deviceType) {
		Params params = Params.param();
		if (app != null) {
			params.put("app", app.getValue());
		}
		if (deviceType != null) {
			params.put("deviceType", deviceType.getValue());
		}
		return yoomathClientVersionRepo.find("$queryVersion", params).list();
	}

	@Transactional
	@Override
	public void updateVersion(CVersionForm form) {
		if (form.getId() != null) {
			// 删除或发布
			if (form.getStatus() != null) {
				YoomathClientVersion yc = this.get(form.getId());
				yc.setStatus(form.getStatus());
				// 表示曾经升级过
				if (form.getStatus() == Status.ENABLED) {
					yc.setUpgradeFlag(true);
				}
				yc.setUpdateAt(new Date());

				yoomathClientVersionRepo.save(yc);
			} else {
				// 状态为空时，表示是编辑
				YoomathClientVersion yc = this.get(form.getId());
				yc.setDescription(form.getDescription());
				yc.setDownloadUrl(form.getDownloadUrl());
				yc.setName(form.getName());
				yc.setSize(form.getSize());
				yc.setType(form.getType());
				yc.setApp(form.getApp());
				yc.setVersion(form.getVersion());
				yc.setVersionNum(Integer.parseInt(form.getVersion().replace(".", "")));
				yc.setUpdateAt(new Date());
				yc.setDeviceType(form.getDeviceType());
				yoomathClientVersionRepo.save(yc);
			}
		} else {
			// 增加
			YoomathClientVersion yc = new YoomathClientVersion();
			yc.setDescription(form.getDescription());
			yc.setDownloadUrl(form.getDownloadUrl());
			yc.setName(form.getName());
			yc.setSize(form.getSize());
			yc.setType(form.getType());
			yc.setApp(form.getApp());
			yc.setVersion(form.getVersion());
			yc.setVersionNum(Integer.parseInt(form.getVersion().replace(".", "")));
			yc.setDeviceType(form.getDeviceType());
			yc.setUpdateAt(new Date());
			yoomathClientVersionRepo.save(yc);
		}

	}

	@Override
	public YoomathClientVersion get(long id) {
		return yoomathClientVersionRepo.get(id);
	}

	@Override
	public boolean versionCount(YooApp app, String version, DeviceType deviceType) {
		Long count = yoomathClientVersionRepo.find("$versionCount",
				Params.param("version", version).put("app", app.getValue()).put("deviceType", deviceType.getValue()))
				.get(Long.class);
		return count > 0 ? true : false;
	}

	@Override
	public boolean isMaxVersionNum(YooApp app, int versionNum, DeviceType deviceType) {
		Integer max = yoomathClientVersionRepo.find("$isMaxVersionNum",
				Params.param("app", app.getValue()).put("deviceType", deviceType.getValue())).get(Integer.class);
		int max1 = max == null ? 0 : max;
		return versionNum > max1 ? true : false;
	}

	@Override
	public YoomathClientVersion findOpenLatestVersion(YooApp app, DeviceType deviceType) {
		return yoomathClientVersionRepo.find("$findOpenLatestVersion",
				Params.param("app", app.getValue()).put("deviceType", deviceType.getValue())).get();
	}
}
