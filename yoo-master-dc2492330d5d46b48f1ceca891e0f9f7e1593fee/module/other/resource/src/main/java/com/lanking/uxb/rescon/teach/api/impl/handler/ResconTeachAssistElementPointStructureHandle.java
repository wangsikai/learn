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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPointStructure;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 知识结构模块handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPointStructureHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPointStructureRepo")
	private Repo<TeachAssistElementPointStructure, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedObject = parseForm(form.getParamForm());
		TeachAssistElementPointStructure structure = null;
		if (form.getId() != null) {
			structure = repo.get(form.getId());
		}
		if (null == structure) {
			structure = new TeachAssistElementPointStructure();
			structure.setCreateAt(new Date());
			structure.setCreateId(form.getUserId());
			structure.setSequence(form.getSequence());
			structure.setTeachAssistCatalogId(form.getCatalogId());
			structure.setType(getType());

			structure.setKnowpointSystem(parsedObject == null ? null : parsedObject.getLong("code"));
			structure.setUpdateAt(new Date());
			structure.setUpdateId(form.getUserId());

			repo.save(structure);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(structure.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			structure.setKnowpointSystem(parsedObject == null ? null : parsedObject.getLong("code"));
			structure.setUpdateAt(new Date());
			structure.setUpdateId(form.getUserId());

			repo.save(structure);
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
		TeachAssistElementPointStructure structure = repo.get(id);
		if (null != structure) {
			structure.setSequence(sequence);
			structure.setUpdateAt(new Date());
			structure.setUpdateId(userId);

			repo.save(structure);
		}
	}

	@Override
	public List<TeachAssistElementPointStructure> get(long catalogId) {
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
		List<TeachAssistElementPointStructure> structures = repo.mgetList(ids);
		for (TeachAssistElementPointStructure s : structures) {
			TeachAssistElementPointStructure n = new TeachAssistElementPointStructure();
			n.setKnowpointSystem(s.getKnowpointSystem());
			n.setCreateAt(now);
			n.setCreateId(userId);
			n.setSequence(s.getSequence());
			n.setTeachAssistCatalogId(newCatalogId);
			n.setType(getType());
			n.setUpdateAt(now);
			n.setUpdateId(userId);

			repo.save(n);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(n.getId());
			ce.setSequence(n.getSequence());
			ce.setTeachassistCatalogId(newCatalogId);
			ce.setType(getType());

			catalogElementRepo.save(ce);
		}
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.POINT_STRUCTURE;
	}
}
