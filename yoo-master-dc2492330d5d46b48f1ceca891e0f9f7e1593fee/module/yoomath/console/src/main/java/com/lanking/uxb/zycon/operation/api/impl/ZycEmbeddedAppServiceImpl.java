package com.lanking.uxb.zycon.operation.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedAppLocation;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.operation.api.ZycEmbeddedAppService;

@Transactional(readOnly = true)
@Service
public class ZycEmbeddedAppServiceImpl implements ZycEmbeddedAppService {

	@Autowired
	@Qualifier("EmbeddedAppRepo")
	private Repo<EmbeddedApp, Long> embeddedAppRepo;

	@Override
	public List<EmbeddedApp> list(YooApp app, EmbeddedAppLocation location) {
		Params params = Params.param("location", location.getValue());
		if (app == null) {
			params.put("app", -1);
		} else {
			params.put("app", app.getValue());
		}
		return embeddedAppRepo.find("$zycList", params).list();
	}

	@Transactional
	@Override
	public List<EmbeddedApp> order(YooApp app, EmbeddedAppLocation location, Long[] ids) {
		List<EmbeddedApp> embeddedApps = list(app, location);
		List<EmbeddedApp> $embeddedApps = new ArrayList<EmbeddedApp>(embeddedApps.size());
		int sequence = 1;
		for (Long id : ids) {
			for (EmbeddedApp embeddedApp : embeddedApps) {
				if (embeddedApp.getId().longValue() == id.longValue()) {
					embeddedApp.setSequence(sequence);
					$embeddedApps.add(embeddedApp);
					break;
				}
			}
			sequence++;
		}
		return embeddedAppRepo.save($embeddedApps);
	}

	@Transactional
	@Override
	public List<EmbeddedApp> del(Long id) {
		EmbeddedApp embeddedApp = embeddedAppRepo.get(id);
		List<EmbeddedApp> embeddedApps = list(embeddedApp.getApp(), embeddedApp.getLocation());
		List<EmbeddedApp> $embeddedApps = new ArrayList<EmbeddedApp>(embeddedApps.size() - 1);
		int sequence = 1;
		for (EmbeddedApp ea : embeddedApps) {
			if (ea.getId().longValue() == id.longValue()) {
				continue;
			}
			ea.setSequence(sequence);
			sequence++;
			$embeddedApps.add(ea);
		}
		embeddedAppRepo.delete(embeddedApp);
		return embeddedAppRepo.save($embeddedApps);
	}

	@Transactional
	@Override
	public EmbeddedApp saveUpdate(Long id, YooApp app, EmbeddedAppLocation location, String name, Long imageId,
			String url) {
		EmbeddedApp embeddedApp = null;
		if (id != null && id > 0) {
			embeddedApp = embeddedAppRepo.get(id);
		}
		if (embeddedApp == null) {
			embeddedApp = new EmbeddedApp();
		}
		embeddedApp.setApp(app);
		embeddedApp.setImageId(imageId);
		embeddedApp.setLocation(location);
		embeddedApp.setName(name);
		if (id == null) {
			List<EmbeddedApp> embeddedApps = list(app, location);
			if (CollectionUtils.isEmpty(embeddedApps)) {
				embeddedApp.setSequence(1);
			} else {
				embeddedApp.setSequence(embeddedApps.get(embeddedApps.size() - 1).getSequence() + 1);
			}
		}
		embeddedApp.setUrl(url);
		return embeddedAppRepo.save(embeddedApp);
	}
}
