package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowKnow;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.rescon.basedata.api.ResconPointService;
import com.lanking.uxb.rescon.basedata.api.ResconPointType;
import com.lanking.uxb.rescon.basedata.form.ResconPointForm;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ResconPointServiceImpl implements ResconPointService {
	@Autowired
	@Qualifier("KnowpointRepo")
	private Repo<Knowpoint, Integer> knowpointRepo;
	@Autowired
	@Qualifier("MetaKnowpointRepo")
	private Repo<MetaKnowpoint, Integer> metaKnowpointRepo;
	@Autowired
	@Qualifier("MetaKnowKnowRepo")
	private Repo<MetaKnowKnow, Integer> metaKnowKnowRepo;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Override
	@Transactional(readOnly = false)
	public void save(ResconPointForm form) {
		// 保存Knowpoint对象->不存在转换的情况
		if (form.getType() == ResconPointType.KNOWPOINT) {
			Knowpoint knowpoint = new Knowpoint();
			knowpoint.setName(form.getName());
			knowpoint.setLevel(form.getLevel() == null ? 1 : form.getLevel());
			knowpoint.setStatus(Status.DISABLED);
			knowpoint.setSubjectCode(form.getSubjectCode());
			knowpoint.setPhaseCode(form.getPhaseCode());
			knowpoint.setPcode(form.getPcode());

			Params params = Params.param();
			params.put("pcode", form.getPcode());
			params.put("subjectCode", form.getSubjectCode());
			Knowpoint lastestKnowpoint = knowpointRepo.find("$findLastestCode", params).get();
			if (lastestKnowpoint == null) {
				knowpoint.setCode(knowpoint.getPcode() * 100 + 11);
			} else {
				knowpoint.setCode(lastestKnowpoint.getCode() + 1);
			}
			knowpointRepo.save(knowpoint);
		} else {
			// 父级的元知识点要转换成知识点类型,并新增元知识点
			if (form.getSwitchType() == true) {
				MetaKnowpoint metaKnowpoint = metaKnowpointRepo.get(form.getPcode());

				Params params = Params.param();
				params.put("metaCode", metaKnowpoint.getCode());
				params.put("knowpointCode", metaKnowpoint.getCode() / 100);

				Knowpoint knowpoint = new Knowpoint();
				knowpoint.setCode(metaKnowpoint.getCode());
				knowpoint.setPcode(metaKnowpoint.getCode() / 100);
				knowpoint.setStatus(Status.DISABLED);
				knowpoint.setName(metaKnowpoint.getName());
				knowpoint.setPhaseCode(metaKnowpoint.getPhaseCode());
				knowpoint.setSubjectCode(metaKnowpoint.getSubjectCode());
				knowpoint.setLevel(form.getLevel());
				knowpointRepo.save(knowpoint);

				metaKnowpointRepo.delete(metaKnowpoint);
				metaKnowKnowRepo.execute("$deleteByKnowpointCodeAndMetaCode", params);

				MetaKnowpoint newMetapoint = new MetaKnowpoint();
				newMetapoint.setName(form.getName());
				newMetapoint.setSubjectCode(form.getSubjectCode());
				newMetapoint.setPhaseCode(form.getPhaseCode());
				newMetapoint.setCode(knowpoint.getCode() * 100 + 11);
				newMetapoint.setStatus(Status.DISABLED);
				metaKnowpointRepo.save(newMetapoint);

				MetaKnowKnow metaKnowKnow = new MetaKnowKnow();
				metaKnowKnow.setKnowPointCode(knowpoint.getCode());
				metaKnowKnow.setMetaCode(newMetapoint.getCode());
				metaKnowKnowRepo.save(metaKnowKnow);
			} else {
				MetaKnowpoint metaKnowpoint = new MetaKnowpoint();
				metaKnowpoint.setStatus(Status.DISABLED);
				metaKnowpoint.setSubjectCode(form.getSubjectCode());
				metaKnowpoint.setPhaseCode(form.getPhaseCode());
				metaKnowpoint.setName(form.getName());

				Params params = Params.param("knowpointCode", form.getPcode() + "%");
				MetaKnowpoint lastestMetapoint = metaKnowpointRepo.find("$findLastestCode", params).get();
				if (lastestMetapoint == null) {
					metaKnowpoint.setCode(form.getPcode() * 100 + 11);
				} else {
					metaKnowpoint.setCode(lastestMetapoint.getCode() + 1);
				}
				metaKnowpointRepo.save(metaKnowpoint);

				MetaKnowKnow metaKnowKnow = new MetaKnowKnow();
				metaKnowKnow.setMetaCode(metaKnowpoint.getCode());
				metaKnowKnow.setKnowPointCode(metaKnowpoint.getCode() / 100);
				metaKnowKnowRepo.save(metaKnowKnow);
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void update(ResconPointForm form) {
		MetaKnowpoint metaKnowpoint = metaKnowpointRepo.get(form.getCode());
		if (metaKnowpoint != null) {
			metaKnowpoint.setName(form.getName());
			metaKnowpointRepo.save(metaKnowpoint);
		} else {
			Knowpoint knowpoint = knowpointRepo.get(form.getCode());
			knowpoint.setName(form.getName());
			knowpointRepo.save(knowpoint);
		}
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.META_KNOWPOINT.name());
		sender.send(event);
		event = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(), BaseDataType.KNOWPOINT.name());
		sender.send(event);
	}

	@Override
	@Transactional
	public void updateSequence(int code, int sequence, ResconPointType type) {
		if (type == ResconPointType.METAKNOWPOINT) {
			MetaKnowpoint metaKnowpoint = metaKnowpointRepo.get(code);
			metaKnowpoint.setSequence(sequence);
			metaKnowpointRepo.save(metaKnowpoint);
		} else {
			Knowpoint knowpoint = knowpointRepo.get(code);
			knowpoint.setSequence(sequence);
			knowpointRepo.save(knowpoint);
		}
	}

	@Override
	@Transactional
	public void turnOn() {
		metaKnowpointRepo.execute("$resconTurnOn", Params.param());
		knowpointRepo.execute("$resconTurnOn", Params.param());
	}

	@Override
	public void updateSequence(List<ResconPointForm> forms) {
		for (ResconPointForm form : forms) {
			if (form.getType() == ResconPointType.METAKNOWPOINT) {
				MetaKnowpoint metaKnowpoint = metaKnowpointRepo.get(form.getCode());
				metaKnowpoint.setSequence(form.getSequence());
				metaKnowpointRepo.save(metaKnowpoint);
			} else {
				Knowpoint knowpoint = knowpointRepo.get(form.getCode());
				knowpoint.setSequence(form.getSequence());
				knowpointRepo.save(knowpoint);
			}
		}
	}

}
