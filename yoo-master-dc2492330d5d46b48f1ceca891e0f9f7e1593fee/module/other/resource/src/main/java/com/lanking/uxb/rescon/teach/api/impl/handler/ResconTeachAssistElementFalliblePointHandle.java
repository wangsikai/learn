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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFalliblePoint;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFalliblePointContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 易错知识点模块handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementFalliblePointHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementFalliblePointRepo")
	private Repo<TeachAssistElementFalliblePoint, Long> repo;
	@Autowired
	@Qualifier("TeachAssistElementFalliblePointContentRepo")
	private Repo<TeachAssistElementFalliblePointContent, Long> contentRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		TeachAssistElementFalliblePoint point = null;
		if (form.getId() != null) {
			point = repo.get(form.getId());
		}
		if (null == point) {
			point = new TeachAssistElementFalliblePoint();
			point.setCreateAt(new Date());
			point.setCreateId(form.getUserId());
			point.setSequence(form.getSequence());
			point.setTeachAssistCatalogId(form.getCatalogId());
			point.setType(getType());
			point.setUpdateAt(new Date());
			point.setUpdateId(form.getUserId());

			repo.save(point);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(point.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			point.setUpdateAt(new Date());
			point.setUpdateId(form.getUserId());
		}

		repo.save(point);
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
		TeachAssistElementFalliblePoint point = repo.get(id);
		if (null != point) {
			point.setSequence(sequence);
			point.setUpdateAt(new Date());
			point.setUpdateId(userId);

			repo.save(point);
		}
	}

	@Override
	public List<TeachAssistElementFalliblePoint> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.FALLIBLE_POINT;
	}

	@Override
	@Transactional
	public void saveContent(TeachAssistElementForm form, long elementId) {
		JSONObject parsedParam = parseForm(form.getParamForm());
		TeachAssistElementFalliblePointContent content = null;
		if (form.getId() != null) {
			content = contentRepo.get(form.getId());
		}
		if (null == content) {
			content = new TeachAssistElementFalliblePointContent();
		}

		if (parsedParam != null) {
			content.setAnalysis(parsedParam.getString("analysis"));
			content.setFallExampleQuestion(parsedParam.getLong("exampleQuestion"));
			content.setFallpointId(elementId);
			content.setName(parsedParam.getString("name"));
			content.setPracticeQuestions(parseList("practiceQuestions", parsedParam));
			content.setSequence(form.getSequence());
			content.setStrategy(parsedParam.getString("strategy"));
			content.setWrongAnalysis(parsedParam.getString("wrongAnalysis"));
			content.setWrongAnswer(parsedParam.getString("wrongAnswer"));
		}

		contentRepo.save(content);
	}

	@Override
	@Transactional
	public void updateContentSequence(long id, int sequence) {
		TeachAssistElementFalliblePointContent content = contentRepo.get(id);
		if (null != content) {
			content.setSequence(sequence);

			contentRepo.save(content);
		}
	}

	@Override
	@Transactional
	public void deleteContent(long id) {
		contentRepo.deleteById(id);
	}

	@Override
	public List getContents(long id) {
		return contentRepo.find("$resconFindByElement", Params.param("elementId", id)).list();
	}

	@Override
	public AbstractTeachAssistElement findOne(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
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
		List<TeachAssistElementFalliblePoint> points = repo.mgetList(ids);
		Map<Long, List<TeachAssistElementFalliblePointContent>> contentMap = new HashMap<Long, List<TeachAssistElementFalliblePointContent>>(
				points.size());

		List<Long> pointIds = new ArrayList<Long>(points.size());
		for (TeachAssistElementFalliblePoint p : points) {
			pointIds.add(p.getId());
		}

		List<TeachAssistElementFalliblePointContent> contents = (List<TeachAssistElementFalliblePointContent>) this
				.mgetContents(pointIds);
		for (TeachAssistElementFalliblePointContent c : contents) {
			List<TeachAssistElementFalliblePointContent> list = contentMap.get(c.getFallpointId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}
			list.add(c);
			contentMap.put(c.getFallpointId(), list);
		}

		for (TeachAssistElementFalliblePoint p : points) {
			TeachAssistElementFalliblePoint newPoint = new TeachAssistElementFalliblePoint();
			newPoint.setCreateAt(now);
			newPoint.setCreateId(userId);
			newPoint.setSequence(p.getSequence());
			newPoint.setTeachAssistCatalogId(newCatalogId);
			newPoint.setType(getType());
			newPoint.setUpdateAt(now);
			newPoint.setUpdateId(userId);

			repo.save(newPoint);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(newPoint.getId());
			ce.setSequence(newPoint.getSequence());
			ce.setTeachassistCatalogId(newCatalogId);
			ce.setType(getType());

			catalogElementRepo.save(ce);

			if (CollectionUtils.isNotEmpty(contentMap.get(p.getId()))) {
				for (TeachAssistElementFalliblePointContent c : contentMap.get(p.getId())) {
					TeachAssistElementFalliblePointContent newPointContent = new TeachAssistElementFalliblePointContent();
					newPointContent.setAnalysis(c.getAnalysis());
					newPointContent.setFallExampleQuestion(c.getFallExampleQuestion());
					newPointContent.setFallpointId(newPoint.getId());
					newPointContent.setName(c.getName());
					newPointContent.setPracticeQuestions(c.getPracticeQuestions());
					newPointContent.setSequence(c.getSequence());
					newPointContent.setStrategy(c.getStrategy());
					newPointContent.setWrongAnalysis(c.getWrongAnalysis());
					newPointContent.setWrongAnswer(c.getWrongAnswer());

					contentRepo.save(newPointContent);
				}
			}
		}
	}
}
