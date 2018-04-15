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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopic;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopicContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopicType;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 专题说明模块handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementTopicHandle extends ResconAbstractTeachAssistElementHandle {

	@Autowired
	@Qualifier("TeachAssistElementTopicRepo")
	private Repo<TeachAssistElementTopic, Long> repo;
	@Autowired
	@Qualifier("TeachAssistElementTopicContentRepo")
	private Repo<TeachAssistElementTopicContent, Long> contentRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedForm = parseForm(form.getParamForm());
		TeachAssistElementTopic topic = null;
		if (form.getId() != null) {
			topic = repo.get(form.getId());
		}
		if (null == topic) {
			topic = new TeachAssistElementTopic();
			topic.setCreateAt(new Date());
			topic.setCreateId(form.getUserId());
			topic.setSequence(form.getSequence());
			topic.setTeachAssistCatalogId(form.getCatalogId());
			topic.setType(getType());

			topic.setUpdateAt(new Date());
			topic.setUpdateId(form.getUserId());
			topic.setTopicType(parsedForm == null ? null : parsedForm.getObject("topicType",
					TeachAssistElementTopicType.class));

			repo.save(topic);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(topic.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			topic.setUpdateAt(new Date());
			topic.setUpdateId(form.getUserId());
			topic.setTopicType(parsedForm == null ? null : parsedForm.getObject("topicType",
					TeachAssistElementTopicType.class));

			repo.save(topic);
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
		TeachAssistElementTopic topic = repo.get(id);
		if (null != topic) {
			topic.setSequence(sequence);
			topic.setUpdateAt(new Date());
			topic.setUpdateId(userId);

			repo.save(topic);
		}

	}

	@Override
	public List<TeachAssistElementTopic> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.TOPIC;
	}

	@Override
	@Transactional
	public void saveContent(TeachAssistElementForm form, long elementId) {
		JSONObject parsedForm = parseForm(form.getParamForm());
		TeachAssistElementTopicContent content = null;
		if (form.getId() != null) {
			content = contentRepo.get(form.getId());
		}
		if (null == content) {
			content = new TeachAssistElementTopicContent();
			content.setSequence(form.getSequence());
			content.setTopicId(elementId);
		}

		if (parsedForm != null) {
			content.setContent(parsedForm.getString("content"));
			content.setName(parsedForm.getString("name"));
			content.setQuestion1(parsedForm.getLong("question1") == null ? null : parsedForm.getLong("question1"));
			content.setQuestion1Strategy(parsedForm.getString("strategy1") == null ? null : parsedForm
					.getString("strategy1"));
			content.setQuestion2(parsedForm.getLong("question2") == null ? null : parsedForm.getLong("question2"));
			content.setQuestion2Strategy(parsedForm.getString("strategy2") == null ? null : parsedForm
					.getString("strategy2"));
			content.setQuestion3(parsedForm.getLong("question3") == null ? null : parsedForm.getLong("question3"));
			content.setQuestion3Strategy(parsedForm.getString("strategy3") == null ? null : parsedForm
					.getString("strategy3"));
		}

		contentRepo.save(content);
	}

	@Override
	@Transactional
	public void updateContentSequence(long id, int sequence) {
		TeachAssistElementTopicContent content = contentRepo.get(id);
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
		List<TeachAssistElementTopic> ts = repo.mgetList(ids);
		List<Long> tIds = new ArrayList<Long>(ts.size());
		Map<Long, List<TeachAssistElementTopicContent>> contentMap = new HashMap<Long, List<TeachAssistElementTopicContent>>(
				tIds.size());

		for (TeachAssistElementTopic t : ts) {
			tIds.add(t.getId());
		}

		List<TeachAssistElementTopicContent> contents = (List<TeachAssistElementTopicContent>) this.mgetContents(tIds);
		for (TeachAssistElementTopicContent c : contents) {
			List<TeachAssistElementTopicContent> list = contentMap.get(c.getTopicId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			list.add(c);
			contentMap.put(c.getTopicId(), list);
		}

		for (TeachAssistElementTopic t : ts) {
			TeachAssistElementTopic n = new TeachAssistElementTopic();
			n.setTopicType(t.getTopicType());
			n.setCreateAt(now);
			n.setCreateId(userId);
			n.setSequence(t.getSequence());
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

			if (CollectionUtils.isNotEmpty(contentMap.get(t.getId()))) {
				for (TeachAssistElementTopicContent c : contentMap.get(t.getId())) {
					TeachAssistElementTopicContent nc = new TeachAssistElementTopicContent();
					nc.setContent(c.getContent());
					nc.setName(c.getName());
					nc.setQuestion1(c.getQuestion1());
					nc.setQuestion1Strategy(c.getQuestion1Strategy());
					nc.setQuestion2(c.getQuestion2());
					nc.setQuestion2Strategy(c.getQuestion2Strategy());
					nc.setQuestion3(c.getQuestion3());
					nc.setQuestion3Strategy(c.getQuestion3Strategy());
					nc.setSequence(c.getSequence());
					nc.setTopicId(n.getId());

					contentRepo.save(nc);
				}
			}
		}

	}
}
