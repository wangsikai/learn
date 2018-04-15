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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPeriodTitle;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 课时标题模块处理handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPeriodTitleHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPeriodTitleRepo")
	private Repo<TeachAssistElementPeriodTitle, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedObject = parseForm(form.getParamForm());
		TeachAssistElementPeriodTitle title = null;
		if (form.getId() != null) {
			title = repo.get(form.getId());
		}
		if (title == null) {
			title = new TeachAssistElementPeriodTitle();
			title.setCreateAt(new Date());
			title.setCreateId(form.getUserId());
			title.setType(getType());
			title.setSequence(form.getSequence());

			title.setTeachAssistCatalogId(form.getCatalogId());
			title.setUpdateAt(new Date());
			title.setUpdateId(form.getUserId());
			title.setTitle(parsedObject == null ? null : parsedObject.getString("title"));

			repo.save(title);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(title.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			title.setTeachAssistCatalogId(form.getCatalogId());
			title.setUpdateAt(new Date());
			title.setUpdateId(form.getUserId());
			title.setTitle(parsedObject == null ? null : parsedObject.getString("title"));

			repo.save(title);
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
		TeachAssistElementPeriodTitle title = repo.get(id);
		if (title != null) {
			title.setSequence(sequence);
			title.setUpdateAt(new Date());
			title.setUpdateId(userId);
			repo.save(title);
		}

	}

	@Override
	public List<TeachAssistElementPeriodTitle> get(long catalogId) {
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
		List<TeachAssistElementPeriodTitle> titles = repo.mgetList(ids);
		for (TeachAssistElementPeriodTitle t : titles) {
			TeachAssistElementPeriodTitle newTitle = new TeachAssistElementPeriodTitle();
			newTitle.setTitle(t.getTitle());
			newTitle.setCreateAt(now);
			newTitle.setCreateId(userId);
			newTitle.setSequence(t.getSequence());
			newTitle.setTeachAssistCatalogId(newCatalogId);
			newTitle.setType(getType());
			newTitle.setUpdateAt(now);
			newTitle.setUpdateId(userId);

			repo.save(newTitle);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(newTitle.getId());
			ce.setSequence(newTitle.getSequence());
			ce.setTeachassistCatalogId(newCatalogId);
			ce.setType(getType());

			catalogElementRepo.save(ce);
		}
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.PERIOD_TITLE;
	}
}
