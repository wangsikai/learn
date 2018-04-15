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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLesson;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLessonPoint;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 课内教学模块
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementLessonHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementLessonRepo")
	private Repo<TeachAssistElementLesson, Long> repo;
	@Autowired
	@Qualifier("TeachAssistElementLessonPointRepo")
	private Repo<TeachAssistElementLessonPoint, Long> lessonPointRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		TeachAssistElementLesson lesson = null;
		if (form.getId() != null) {
			lesson = repo.get(form.getId());
		}
		if (null == lesson) {
			lesson = new TeachAssistElementLesson();

			lesson.setCreateAt(new Date());
			lesson.setCreateId(form.getUserId());
			lesson.setSequence(form.getSequence());
			lesson.setTeachAssistCatalogId(form.getCatalogId());
			lesson.setType(getType());

			lesson.setUpdateAt(new Date());
			lesson.setUpdateId(form.getUserId());

			repo.save(lesson);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(lesson.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			lesson.setUpdateAt(new Date());
			lesson.setUpdateId(form.getUserId());

			repo.save(lesson);
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
		TeachAssistElementLesson lesson = repo.get(id);
		if (null != lesson) {
			lesson.setSequence(sequence);
			lesson.setUpdateAt(new Date());
			lesson.setUpdateId(userId);

			repo.save(lesson);
		}
	}

	@Override
	public List<TeachAssistElementLesson> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.LESSON_TEACH;
	}

	@Override
	@Transactional
	public void saveContent(TeachAssistElementForm form, long elementId) {
		JSONObject parsedParam = parseForm(form.getParamForm());
		TeachAssistElementLessonPoint point = null;
		if (form.getId() != null) {
			point = lessonPointRepo.get(form.getId());
		}
		if (point == null) {
			point = new TeachAssistElementLessonPoint();
			point.setLessonId(elementId);
		}

		if (null != parsedParam) {
			point.setExampleQuestions(parseList("exampleQuestions", parsedParam));
			point.setExpandQuestions(parseList("expandQuestions", parsedParam));
			point.setName(parsedParam.getString("name"));
			point.setKnowpoints(parseList("points", parsedParam));
			point.setSequence(parsedParam.getInteger("sequence"));
		}

		lessonPointRepo.save(point);
	}

	@Override
	@Transactional
	public void updateContentSequence(long id, int sequence) {
		TeachAssistElementLessonPoint point = lessonPointRepo.get(id);
		if (null != point) {
			point.setSequence(sequence);

			lessonPointRepo.save(point);
		}
	}

	@Override
	@Transactional
	public void deleteContent(long id) {
		lessonPointRepo.deleteById(id);
	}

	@Override
	public List getContents(long id) {
		return lessonPointRepo.find("$resconFindByElement", Params.param("elementId", id)).list();
	}

	@Override
	public AbstractTeachAssistElement findOne(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void deleteByElement(long elementId) {
		lessonPointRepo.execute("$resconDeleteByElement", Params.param("elementId", elementId));
	}

	@Override
	public List mgetContents(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}

		return lessonPointRepo.find("$resconFindByElements", Params.param("elementIds", ids)).list();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void copy(long newCatalogId, Collection<Long> ids, long userId) {
		Date now = new Date();
		List<TeachAssistElementLesson> lessons = repo.mgetList(ids);
		Map<Long, List<TeachAssistElementLessonPoint>> contentMap = new HashMap<Long, List<TeachAssistElementLessonPoint>>(
				lessons.size());
		List<Long> lessonIds = new ArrayList<Long>(lessons.size());

		for (TeachAssistElementLesson l : lessons) {
			lessonIds.add(l.getId());
		}

		List<TeachAssistElementLessonPoint> points = (List<TeachAssistElementLessonPoint>) this.mgetContents(lessonIds);
		for (TeachAssistElementLessonPoint p : points) {
			List<TeachAssistElementLessonPoint> list = contentMap.get(p.getLessonId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			list.add(p);
			contentMap.put(p.getLessonId(), list);
		}

		for (TeachAssistElementLesson l : lessons) {
			TeachAssistElementLesson newLesson = new TeachAssistElementLesson();
			newLesson.setCreateAt(now);
			newLesson.setCreateId(userId);
			newLesson.setSequence(l.getSequence());
			newLesson.setTeachAssistCatalogId(newCatalogId);
			newLesson.setType(getType());
			newLesson.setUpdateAt(now);
			newLesson.setUpdateId(userId);

			repo.save(newLesson);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(newLesson.getId());
			ce.setSequence(newLesson.getSequence());
			ce.setTeachassistCatalogId(newCatalogId);
			ce.setType(getType());

			catalogElementRepo.save(ce);

			if (CollectionUtils.isNotEmpty(contentMap.get(l.getId()))) {
				for (TeachAssistElementLessonPoint p : contentMap.get(l.getId())) {
					TeachAssistElementLessonPoint newPoint = new TeachAssistElementLessonPoint();
					newPoint.setExampleQuestions(p.getExampleQuestions());
					newPoint.setExpandQuestions(p.getExpandQuestions());
					newPoint.setKnowpoints(p.getKnowpoints());
					newPoint.setLessonId(newLesson.getId());
					newPoint.setName(p.getName());
					newPoint.setSequence(p.getSequence());

					lessonPointRepo.save(newPoint);
				}
			}
		}
	}
}
