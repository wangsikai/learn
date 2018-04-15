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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLearnGoal;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 学习目标handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementLearnGoalHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementLearnGoalRepo")
	private Repo<TeachAssistElementLearnGoal, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedForm = parseForm(form.getParamForm());

		TeachAssistElementLearnGoal learnGoal = null;
		if (form.getId() != null) {
			learnGoal = repo.get(form.getId());
		}
		if (learnGoal == null) {
			learnGoal = new TeachAssistElementLearnGoal();
			learnGoal.setCreateAt(new Date());
			learnGoal.setCreateId(form.getUserId());
			learnGoal.setType(getType());
			learnGoal.setSequence(form.getSequence());
			learnGoal.setTeachAssistCatalogId(form.getCatalogId());
			learnGoal.setType(getType());
			learnGoal.setContent(parsedForm == null ? null : parsedForm.getString("content"));
			learnGoal.setUpdateAt(new Date());
			learnGoal.setUpdateId(form.getUserId());

			repo.save(learnGoal);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(learnGoal.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			learnGoal.setContent(parsedForm == null ? null : parsedForm.getString("content"));
			learnGoal.setUpdateAt(new Date());
			learnGoal.setUpdateId(form.getUserId());

			repo.save(learnGoal);
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
		TeachAssistElementLearnGoal learnGoal = repo.get(id);
		if (learnGoal != null) {
			learnGoal.setSequence(sequence);
			learnGoal.setUpdateAt(new Date());
			learnGoal.setUpdateId(userId);
		}

		repo.save(learnGoal);
	}

	@Override
	public List<TeachAssistElementLearnGoal> get(long catalogId) {
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
		List<TeachAssistElementLearnGoal> goals = repo.mgetList(ids);

		for (TeachAssistElementLearnGoal g : goals) {
			TeachAssistElementLearnGoal newGoal = new TeachAssistElementLearnGoal();
			newGoal.setContent(g.getContent());
			newGoal.setCreateAt(now);
			newGoal.setCreateId(userId);
			newGoal.setSequence(g.getSequence());
			newGoal.setTeachAssistCatalogId(newCatalogId);
			newGoal.setType(getType());
			newGoal.setUpdateAt(now);
			newGoal.setUpdateId(userId);

			repo.save(newGoal);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(newGoal.getId());
			ce.setSequence(newGoal.getSequence());
			ce.setTeachassistCatalogId(newCatalogId);
			ce.setType(getType());

			catalogElementRepo.save(ce);
		}
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.LEARN_GOAL;
	}
}
