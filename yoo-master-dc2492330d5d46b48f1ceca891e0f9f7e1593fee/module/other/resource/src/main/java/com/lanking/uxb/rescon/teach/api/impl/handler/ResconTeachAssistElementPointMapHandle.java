package com.lanking.uxb.rescon.teach.api.impl.handler;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPointMap;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 知识拓扑结构模块
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPointMapHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPointMapRepo")
	private Repo<TeachAssistElementPointMap, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedForm = parseForm(form.getParamForm());
		TeachAssistElementPointMap map = null;
		if (form.getId() != null) {
			map = repo.get(form.getId());
		}
		if (null == map) {
			map = new TeachAssistElementPointMap();
			map.setCreateAt(new Date());
			map.setCreateId(form.getUserId());
			map.setSequence(form.getSequence());
			map.setTeachAssistCatalogId(form.getCatalogId());
			map.setType(getType());

			map.setKnowpointSystem(parsedForm == null ? null : parsedForm.getLong("code"));
			map.setUpdateAt(new Date());
			map.setUpdateId(form.getUserId());

			repo.save(map);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(map.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			map.setKnowpointSystem(parsedForm == null ? null : parsedForm.getLong("code"));
			map.setUpdateAt(new Date());
			map.setUpdateId(form.getUserId());

			repo.save(map);
		}

	}

	@Override
	@Transactional
	public void delete(TeachAssistElementForm form) {
		repo.deleteById(form.getId());
	}

	@Override
	@Transactional
	public void sequence(long id, int sequence, long userId) {
		TeachAssistElementPointMap map = repo.get(id);
		if (null != map) {
			map.setUpdateAt(new Date());
			map.setUpdateId(userId);
			map.setSequence(sequence);

			repo.save(map);
		}
	}

	@Override
	public List<TeachAssistElementPointMap> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public AbstractTeachAssistElement findOne(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void copy(long newCatalogId, Collection<Long> ids, long userId) {
		Date now = new Date();
		List<TeachAssistElementPointMap> maps = repo.mgetList(ids);
		for (TeachAssistElementPointMap m : maps) {
			TeachAssistElementPointMap newMap = new TeachAssistElementPointMap();
			newMap.setKnowpointSystem(m.getKnowpointSystem());
			newMap.setCreateAt(now);
			newMap.setCreateId(userId);
			newMap.setSequence(m.getSequence());
			newMap.setTeachAssistCatalogId(newCatalogId);
			newMap.setType(getType());
			newMap.setUpdateAt(now);
			newMap.setUpdateId(userId);

			repo.save(newMap);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(newMap.getId());
			ce.setSequence(newMap.getSequence());
			ce.setTeachassistCatalogId(newCatalogId);
			ce.setType(getType());

			catalogElementRepo.save(ce);
		}
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.POINT_MAP;
	}
}
