package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.rescon.basedata.api.ResconKnowpointService;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ResconKnowpointServiceImpl implements ResconKnowpointService {
	@Autowired
	@Qualifier("KnowpointRepo")
	private Repo<Knowpoint, Integer> repo;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Override
	public List<Knowpoint> find(Integer pcode, Integer subjectCode, Integer phaseCode) {
		Params params = Params.param();
		params.put("pcode", pcode);
		params.put("subjectCode", subjectCode);
		params.put("phaseCode", phaseCode);
		List<Knowpoint> list = repo.find("$findByPSAndPcode", params).list();
		return list;
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.KNOWPOINT.name());
		sender.send(event);
	}
}
