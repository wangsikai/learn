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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPractice;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPracticeContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 习题内容模块handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPracticeHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPracticeRepo")
	private Repo<TeachAssistElementPractice, Long> repo;
	@Autowired
	@Qualifier("TeachAssistElementPracticeContentRepo")
	private Repo<TeachAssistElementPracticeContent, Long> practiceContentRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		TeachAssistElementPractice practice = null;
		if (form.getId() != null) {
			practice = repo.get(form.getId());
		}
		if (null == practice) {
			practice = new TeachAssistElementPractice();
			practice.setCreateAt(new Date());
			practice.setCreateId(form.getUserId());
			practice.setSequence(form.getSequence());
			practice.setTeachAssistCatalogId(form.getCatalogId());
			practice.setType(getType());

			practice.setUpdateAt(new Date());
			practice.setUpdateId(form.getUserId());

			repo.save(practice);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(practice.getId());
			ce.setSequence(form.getSequence());
			ce.setType(getType());
			ce.setTeachassistCatalogId(form.getCatalogId());

			catalogElementRepo.save(ce);
		} else {

			practice.setUpdateAt(new Date());
			practice.setUpdateId(form.getUserId());

			repo.save(practice);
		}

	}

	@Override
	@Transactional
	public void delete(TeachAssistElementForm form) {
		repo.deleteById(form.getId());
		deleteByElement(form.getId());
	}

	@Override
	@Transactional
	public void sequence(long id, int sequence, long userId) {
		TeachAssistElementPractice practice = repo.get(id);
		if (null != practice) {
			practice.setSequence(sequence);
			practice.setUpdateAt(new Date());
			practice.setUpdateId(userId);

			repo.save(practice);
		}
	}

	@Override
	public List<TeachAssistElementPractice> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.PRACTICE;
	}

	@Override
	@Transactional
	public void saveContent(TeachAssistElementForm form, long elementId) {
		JSONObject parsedParam = parseForm(form.getParamForm());
		TeachAssistElementPracticeContent content = null;
		if (form.getId() != null) {
			content = practiceContentRepo.get(form.getId());
		}
		if (null == content) {
			content = new TeachAssistElementPracticeContent();
			content.setPracticeId(elementId);
			content.setSequence(form.getSequence());

		}
		if (parsedParam != null) {
			content.setName(parsedParam.getString("name"));
			content.setQuestions(parseList("questions", parsedParam));
		}

		practiceContentRepo.save(content);
	}

	@Override
	@Transactional
	public void updateContentSequence(long id, int sequence) {
		TeachAssistElementPracticeContent content = practiceContentRepo.get(id);
		if (null != content) {
			content.setSequence(sequence);

			practiceContentRepo.save(content);
		}
	}

	@Override
	@Transactional
	public void deleteContent(long id) {
		practiceContentRepo.deleteById(id);
	}

	@Override
	public List getContents(long id) {
		return practiceContentRepo.find("$resconFindByElement", Params.param("elementId", id)).list();
	}

	@Override
	public AbstractTeachAssistElement findOne(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void deleteByElement(long elementId) {
		practiceContentRepo.execute("$resconDeleteByElement", Params.param("elementId", elementId));
	}

	@Override
	public List mgetContents(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}

		return practiceContentRepo.find("$resconFindByElements", Params.param("elementIds", ids)).list();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void copy(long newCatalogId, Collection<Long> ids, long userId) {
		Date now = new Date();
		List<TeachAssistElementPractice> ps = repo.mgetList(ids);
		List<Long> pIds = new ArrayList<Long>(ps.size());
		Map<Long, List<TeachAssistElementPracticeContent>> contentMap = new HashMap<Long, List<TeachAssistElementPracticeContent>>(
				ps.size());

		for (TeachAssistElementPractice p : ps) {
			pIds.add(p.getId());
		}

		List<TeachAssistElementPracticeContent> contents = (List<TeachAssistElementPracticeContent>) this
				.mgetContents(pIds);
		for (TeachAssistElementPracticeContent c : contents) {
			List<TeachAssistElementPracticeContent> list = contentMap.get(c.getPracticeId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			list.add(c);
			contentMap.put(c.getPracticeId(), list);
		}

		for (TeachAssistElementPractice p : ps) {
			TeachAssistElementPractice n = new TeachAssistElementPractice();
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

			if (CollectionUtils.isNotEmpty(contentMap.get(p.getId()))) {
				for (TeachAssistElementPracticeContent c : contentMap.get(p.getId())) {
					TeachAssistElementPracticeContent nc = new TeachAssistElementPracticeContent();
					nc.setName(c.getName());
					nc.setPracticeId(n.getId());
					nc.setQuestions(c.getQuestions());
					nc.setSequence(c.getSequence());

					practiceContentRepo.save(nc);
				}
			}

		}
	}
}
