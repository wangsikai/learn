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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementProblemSolving;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 解题方法及要点小结 模块
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementProblemSolvingHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementProblemSolvingRepo")
	private Repo<TeachAssistElementProblemSolving, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedForm = parseForm(form.getParamForm());
		TeachAssistElementProblemSolving obj = null;
		if (form.getId() != null) {
			obj = repo.get(form.getId());
		}
		if (obj == null) {
			obj = new TeachAssistElementProblemSolving();
			obj.setCreateAt(new Date());
			obj.setCreateId(form.getUserId());
			obj.setSequence(form.getSequence());
			obj.setTeachAssistCatalogId(form.getCatalogId());
			obj.setType(getType());

			obj.setContent(parsedForm == null ? null : parsedForm.getString("content"));
			obj.setUpdateAt(new Date());
			obj.setUpdateId(form.getUserId());

			repo.save(obj);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(obj.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			obj.setContent(parsedForm == null ? null : parsedForm.getString("content"));
			obj.setUpdateAt(new Date());
			obj.setUpdateId(form.getUserId());

			repo.save(obj);
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
		TeachAssistElementProblemSolving obj = repo.get(id);
		if (null != obj) {
			obj.setUpdateAt(new Date());
			obj.setUpdateId(userId);
			obj.setSequence(sequence);

			repo.save(obj);
		}
	}

	@Override
	public List<TeachAssistElementProblemSolving> get(long catalogId) {
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
		List<TeachAssistElementProblemSolving> ps = repo.mgetList(ids);
		for (TeachAssistElementProblemSolving p : ps) {
			TeachAssistElementProblemSolving n = new TeachAssistElementProblemSolving();
			n.setContent(p.getContent());
			n.setCreateAt(now);
			n.setCreateId(userId);
			n.setSequence(p.getSequence());
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
		return TeachAssistElementType.PROBLEM_SOLVING;
	}
}
