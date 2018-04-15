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
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementKnowledgeSpec;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementKnowledgeSpecKp;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointCardService;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 知识说明/回顾模块
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementKnowledgeSpecHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementKnowledgeSpecRepo")
	private Repo<TeachAssistElementKnowledgeSpec, Long> repo;
	@Autowired
	@Qualifier("TeachAssistElementKnowledgeSpecKpRepo")
	private Repo<TeachAssistElementKnowledgeSpecKp, Long> kpRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;
	@Autowired
	private ResconKnowledgePointCardService knowledgePointCardService;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedObj = parseForm(form.getParamForm());
		TeachAssistElementKnowledgeSpec spec = null;
		if (form.getId() != null) {
			spec = repo.get(form.getId());
		}
		if (null == spec) {
			spec = new TeachAssistElementKnowledgeSpec();
			spec.setCreateAt(new Date());
			spec.setCreateId(form.getUserId());
			spec.setSequence(form.getSequence());
			spec.setTeachAssistCatalogId(form.getCatalogId());
			spec.setType(getType());
			spec.setReview(parsedObj == null ? false : parsedObj.getBoolean("review"));
			spec.setUpdateAt(new Date());
			spec.setUpdateId(form.getUserId());

			repo.save(spec);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(spec.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			spec.setReview(parsedObj == null ? false : parsedObj.getBoolean("review"));
			spec.setUpdateAt(new Date());
			spec.setUpdateId(form.getUserId());

			repo.save(spec);
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
		TeachAssistElementKnowledgeSpec spec = repo.get(id);
		if (null != spec) {
			spec.setUpdateAt(new Date());
			spec.setUpdateId(userId);
			spec.setSequence(sequence);

			repo.save(spec);
		}
	}

	@Override
	public List<TeachAssistElementKnowledgeSpec> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	@Transactional
	public void saveContent(TeachAssistElementForm form, long elementId) {
		JSONObject parsedParam = parseForm(form.getParamForm());
		TeachAssistElementKnowledgeSpecKp kp = null;
		if (form.getId() != null) {
			kp = kpRepo.get(form.getId());
		}
		if (null == kp) {
			kp = new TeachAssistElementKnowledgeSpecKp();
			kp.setKnowledgeSpecId(elementId);
		}

		if (null != parsedParam) {
			Long knowledgeCode = parsedParam.getLong("knowledgeCode");
			kp.setKnowledgeCode(knowledgeCode);
			// 若首次添加则去拉取知识点下所有较验通过的卡片描述字段填充进来
			if (StringUtils.isBlank(parsedParam.getString("content")) && kp.getId() == null) {
				List<KnowledgePointCard> cards = knowledgePointCardService.findByKnowpointCode(knowledgeCode,
						CardStatus.PASS);

				StringBuilder contentBuilder = new StringBuilder();
				for (KnowledgePointCard c : cards) {
					contentBuilder.append(c.getDescription());
				}

				kp.setContent(contentBuilder.toString());
			} else {
				kp.setContent(parsedParam.getString("content"));
			}
			kp.setSequence(form.getSequence());
		}

		kpRepo.save(kp);
	}

	@Override
	@Transactional
	public void updateContentSequence(long id, int sequence) {
		TeachAssistElementKnowledgeSpecKp kp = kpRepo.get(id);
		if (null != kp) {
			kp.setSequence(sequence);

			kpRepo.save(kp);
		}
	}

	@Override
	@Transactional
	public void deleteContent(long id) {
		kpRepo.deleteById(id);
	}

	@Override
	public List getContents(long id) {
		return kpRepo.find("$resconFindByElement", Params.param("id", id)).list();
	}

	@Override
	public AbstractTeachAssistElement findOne(long id) {
		return repo.get(id);
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.KNOWLEDGE_SPEC;
	}

	@Override
	@Transactional
	public void deleteByElement(long elementId) {
		kpRepo.execute("$resconDeleteByElement", Params.param("elementId", elementId));
	}

	@Override
	public List mgetContents(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}

		return kpRepo.find("$resconFindByElements", Params.param("elementIds", ids)).list();
	}

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public void copy(long newCatalogId, Collection<Long> ids, long userId) {
		Date now = new Date();
		List<TeachAssistElementKnowledgeSpec> ks = repo.mgetList(ids);
		Map<Long, List<TeachAssistElementKnowledgeSpecKp>> contentMap = new HashMap<Long, List<TeachAssistElementKnowledgeSpecKp>>(
				ks.size());
		List<Long> kIds = new ArrayList<Long>(ks.size());

		for (TeachAssistElementKnowledgeSpec k : ks) {
			kIds.add(k.getId());
		}

		List<TeachAssistElementKnowledgeSpecKp> kps = (List<TeachAssistElementKnowledgeSpecKp>) this.mgetContents(kIds);
		for (TeachAssistElementKnowledgeSpecKp kp : kps) {
			List<TeachAssistElementKnowledgeSpecKp> list = contentMap.get(kp.getKnowledgeSpecId());
			if (CollectionUtils.isEmpty(list)) {
				list = Lists.newArrayList();
			}

			list.add(kp);
			contentMap.put(kp.getKnowledgeSpecId(), list);
		}

		for (TeachAssistElementKnowledgeSpec k : ks) {
			TeachAssistElementKnowledgeSpec newObj = new TeachAssistElementKnowledgeSpec();
			newObj.setReview(k.isReview());
			newObj.setCreateAt(now);
			newObj.setCreateId(userId);
			newObj.setSequence(k.getSequence());
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

			if (CollectionUtils.isNotEmpty(contentMap.get(k.getId()))) {
				for (TeachAssistElementKnowledgeSpecKp kp : contentMap.get(k.getId())) {
					TeachAssistElementKnowledgeSpecKp newKp = new TeachAssistElementKnowledgeSpecKp();
					newKp.setContent(kp.getContent());
					newKp.setKnowledgeCode(kp.getKnowledgeCode());
					newKp.setKnowledgeSpecId(newObj.getId());
					newKp.setSequence(kp.getSequence());

					kpRepo.save(newKp);
				}
			}
		}
	}
}
