package com.lanking.uxb.rescon.teach.api.impl.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoal;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoalContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 预习目标 模块
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPrepareGoalHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPrepareGoalRepo")
	private Repo<TeachAssistElementPrepareGoal, Long> repo;
	@Autowired
	@Qualifier("TeachAssistElementPrepareGoalContentRepo")
	private Repo<TeachAssistElementPrepareGoalContent, Long> contentRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		TeachAssistElementPrepareGoal goal = null;
		if (form.getId() != null) {
			goal = repo.get(form.getId());
		}
		if (null == goal) {
			goal = new TeachAssistElementPrepareGoal();
			goal.setSequence(form.getSequence());
			goal.setType(getType());
			goal.setCreateAt(new Date());
			goal.setCreateId(form.getUserId());
			goal.setUpdateAt(new Date());
			goal.setUpdateId(form.getUserId());
			goal.setTeachAssistCatalogId(form.getCatalogId());
			repo.save(goal);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(goal.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			goal.setUpdateAt(new Date());
			goal.setUpdateId(form.getUserId());

			repo.save(goal);
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
		TeachAssistElementPrepareGoal goal = repo.get(id);
		if (null != goal) {
			goal.setSequence(sequence);
			goal.setUpdateAt(new Date());
			goal.setUpdateId(userId);

			repo.save(goal);
		}
	}

	@Override
	public List<TeachAssistElementPrepareGoal> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public AbstractTeachAssistElement findOne(long id) {
		return repo.get(id);
	}

	@Override
	public void saveContent(TeachAssistElementForm form, long elementId) {
		JSONObject parsedParam = parseForm(form.getParamForm());
		TeachAssistElementPrepareGoalContent content = null;
		if (form.getId() != null) {
			content = contentRepo.get(form.getId());
		}
		if (content == null) {
			content = new TeachAssistElementPrepareGoalContent();
			content.setGoalId(elementId);
			content.setSequence(form.getSequence());
		}

		if (parsedParam != null) {
			content.setSelfTestQuestions(parseList("selfTestQuestions", parsedParam));
			content.setName(parsedParam.getString("name"));
			content.setKnowpoints(parseList("knowpoints", parsedParam));
			content.setPreviewQuestions(parseList("previewQuestions", parsedParam));
		}

		contentRepo.save(content);
	}

	@Override
	public void updateContentSequence(long id, int sequence) {
		TeachAssistElementPrepareGoalContent content = contentRepo.get(id);
		if (content != null) {
			content.setSequence(sequence);

			contentRepo.save(content);
		}
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.PREPARE_GOAL;
	}

	@Override
	public void deleteContent(long id) {
		contentRepo.deleteById(id);
	}

	@Override
	public List getContents(long id) {
		return contentRepo.find("$resconFindByElement", Params.param("elementId", id)).list();
	}

	@Override
	public void deleteByElement(long elementId) {
		contentRepo.execute("$resconDeleteByElement", Params.param("elementId", elementId));
	}

	@Override
	public List mgetContents(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}

		return contentRepo.find("$resconFindByElements", Params.param("elementIds", ids)).list();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void copy(long newCatalogId, Collection<Long> ids, long userId) {
		Date now = new Date();
		List<TeachAssistElementPrepareGoal> goals = repo.mgetList(ids);
		List<Long> gIds = new ArrayList<Long>(goals.size());
		Map<Long, List<TeachAssistElementPrepareGoalContent>> contentMap = new HashMap<Long, List<TeachAssistElementPrepareGoalContent>>(
				goals.size());
		for (TeachAssistElementPrepareGoal g : goals) {
			gIds.add(g.getId());
		}

		List<TeachAssistElementPrepareGoalContent> contents = (List<TeachAssistElementPrepareGoalContent>) this
				.mgetContents(gIds);
		for (TeachAssistElementPrepareGoalContent c : contents) {
			List<TeachAssistElementPrepareGoalContent> list = contentMap.get(c.getGoalId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			list.add(c);
			contentMap.put(c.getGoalId(), list);
		}

		for (TeachAssistElementPrepareGoal g : goals) {
			TeachAssistElementPrepareGoal n = new TeachAssistElementPrepareGoal();
			n.setCreateAt(now);
			n.setCreateId(userId);
			n.setSequence(g.getSequence());
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

			if (CollectionUtils.isNotEmpty(contentMap.get(g.getId()))) {
				for (TeachAssistElementPrepareGoalContent c : contentMap.get(g.getId())) {
					TeachAssistElementPrepareGoalContent nc = new TeachAssistElementPrepareGoalContent();
					nc.setGoalId(n.getId());
					nc.setKnowpoints(c.getKnowpoints());
					nc.setName(c.getName());
					nc.setPreviewQuestions(c.getPreviewQuestions());
					nc.setSelfTestQuestions(c.getSelfTestQuestions());
					nc.setSequence(c.getSequence());

					contentRepo.save(nc);
				}
			}
		}

	}
}
