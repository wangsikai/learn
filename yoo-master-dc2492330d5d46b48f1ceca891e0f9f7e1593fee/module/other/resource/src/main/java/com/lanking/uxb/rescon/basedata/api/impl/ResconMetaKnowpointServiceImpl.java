package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.api.ResconMetaKnowpointService;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.MetaKnowKnowService;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ResconMetaKnowpointServiceImpl implements ResconMetaKnowpointService {
	@Autowired
	@Qualifier("MetaKnowpointRepo")
	private Repo<MetaKnowpoint, Integer> repo;
	@Autowired
	private MetaKnowKnowService metaKnowKnowService;
	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Override
	public List<MetaKnowpoint> findAll(Integer knowPointCode) {
		return null;
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.META_KNOWPOINT.name());
		sender.send(event);
	}

	@Override
	public List<MetaKnowpoint> findAll(Collection<Integer> knowpointCodes) {
		if (CollectionUtils.isEmpty(knowpointCodes)) {
			return Lists.newArrayList();
		}
		Params params = Params.param("knowpointCodes", knowpointCodes);
		List<MetaKnowpoint> points = repo.find("$mgetListByKnowpointCodes", params).list();
		return points;
	}
}
