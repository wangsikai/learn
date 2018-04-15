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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFreeEdit;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFreeEditContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 自由编辑模块handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementFreeEditHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementFreeEditRepo")
	private Repo<TeachAssistElementFreeEdit, Long> repo;
	@Autowired
	@Qualifier("TeachAssistElementFreeEditContentRepo")
	private Repo<TeachAssistElementFreeEditContent, Long> contentRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedForm = parseForm(form.getParamForm());
		TeachAssistElementFreeEdit freeEdit = null;
		if (form.getId() != null) {
			freeEdit = repo.get(form.getId());
		}
		if (null == freeEdit) {
			freeEdit = new TeachAssistElementFreeEdit();
			freeEdit.setCreateAt(new Date());
			freeEdit.setCreateId(form.getUserId());
			freeEdit.setSequence(form.getSequence());
			freeEdit.setType(getType());
			freeEdit.setTeachAssistCatalogId(form.getCatalogId());
			freeEdit.setUpdateAt(new Date());
			freeEdit.setUpdateId(form.getUserId());
			freeEdit.setName(parsedForm == null ? "" : parsedForm.getString("name"));

			repo.save(freeEdit);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(freeEdit.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			freeEdit.setUpdateAt(new Date());
			freeEdit.setUpdateId(form.getUserId());
			freeEdit.setName(parsedForm == null ? "" : parsedForm.getString("name"));

			repo.save(freeEdit);
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
		TeachAssistElementFreeEdit freeEdit = repo.get(id);
		if (null != freeEdit) {
			freeEdit.setUpdateAt(new Date());
			freeEdit.setUpdateId(userId);
			freeEdit.setSequence(sequence);

			repo.save(freeEdit);
		}
	}

	@Override
	public List<TeachAssistElementFreeEdit> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.FREE_EDIT;
	}

	@Override
	public void saveContent(TeachAssistElementForm form, long elementId) {
		JSONObject parsedParam = parseForm(form.getParamForm());
		TeachAssistElementFreeEditContent content = null;
		if (form.getId() != null) {
			content = contentRepo.get(form.getId());
		}
		if (null == content) {
			content = new TeachAssistElementFreeEditContent();
		}
		if (parsedParam != null) {
			content.setContent(parsedParam.getString("content"));
			content.setSequence(parsedParam.getInteger("sequence"));
			content.setFreeEditId(elementId);
			content.setQuestionId(parsedParam.getLong("questionId"));
		}

		contentRepo.save(content);
	}

	@Override
	public void updateContentSequence(long id, int sequence) {
		TeachAssistElementFreeEditContent content = contentRepo.get(id);
		if (null != content) {
			content.setSequence(sequence);

			contentRepo.save(content);
		}
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
		List<TeachAssistElementFreeEdit> es = repo.mgetList(ids);
		Map<Long, List<TeachAssistElementFreeEditContent>> contentMap = new HashMap<Long, List<TeachAssistElementFreeEditContent>>(
				es.size());

		List<Long> eIds = new ArrayList<Long>(es.size());
		for (TeachAssistElementFreeEdit e : es) {
			eIds.add(e.getId());
		}

		List<TeachAssistElementFreeEditContent> cs = (List<TeachAssistElementFreeEditContent>) this.mgetContents(eIds);
		for (TeachAssistElementFreeEditContent c : cs) {
			List<TeachAssistElementFreeEditContent> list = contentMap.get(c.getFreeEditId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			list.add(c);
			contentMap.put(c.getFreeEditId(), list);
		}

		for (TeachAssistElementFreeEdit e : es) {
			TeachAssistElementFreeEdit newObj = new TeachAssistElementFreeEdit();
			newObj.setName(e.getName());
			newObj.setCreateAt(now);
			newObj.setCreateId(userId);
			newObj.setSequence(e.getSequence());
			newObj.setTeachAssistCatalogId(newCatalogId);
			newObj.setType(getType());
			newObj.setUpdateAt(now);
			newObj.setUpdateId(userId);

			repo.save(newObj);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(newObj.getId());
			ce.setSequence(newObj.getSequence());
			ce.setTeachassistCatalogId(newCatalogId);
			ce.setType(getType());

			catalogElementRepo.save(ce);

			if (CollectionUtils.isNotEmpty(contentMap.get(e.getId()))) {
				for (TeachAssistElementFreeEditContent c : contentMap.get(e.getId())) {
					TeachAssistElementFreeEditContent newContent = new TeachAssistElementFreeEditContent();
					newContent.setContent(c.getContent());
					newContent.setFreeEditId(newObj.getId());
					newContent.setQuestionId(c.getQuestionId());
					newContent.setSequence(c.getSequence());

					contentRepo.save(newContent);
				}
			}
		}
	}
}
