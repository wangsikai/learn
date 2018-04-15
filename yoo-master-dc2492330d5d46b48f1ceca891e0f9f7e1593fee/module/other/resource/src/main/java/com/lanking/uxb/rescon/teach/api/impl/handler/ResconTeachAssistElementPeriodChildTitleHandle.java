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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPeriodChildTitle;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 课时子标题Handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPeriodChildTitleHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPeriodChildTitleRepo")
	private Repo<TeachAssistElementPeriodChildTitle, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		JSONObject parsedForm = parseForm(form.getParamForm());
		TeachAssistElementPeriodChildTitle ctitle = null;
		if (form.getId() != null) {
			ctitle = repo.get(form.getId());
		}
		if (ctitle == null) {
			ctitle = new TeachAssistElementPeriodChildTitle();
			ctitle.setCreateAt(new Date());
			ctitle.setCreateId(form.getUserId());
			ctitle.setType(getType());
			ctitle.setSequence(form.getSequence());
			ctitle.setTeachAssistCatalogId(form.getCatalogId());

			ctitle.setUpdateAt(new Date());
			ctitle.setUpdateId(form.getUserId());
			ctitle.setTitle(parsedForm == null ? null : parsedForm.getString("title"));

			repo.save(ctitle);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(ctitle.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(form.getType());

			catalogElementRepo.save(ce);
		} else {
			ctitle.setUpdateAt(new Date());
			ctitle.setUpdateId(form.getUserId());
			ctitle.setTitle(parsedForm == null ? null : parsedForm.getString("title"));

			repo.save(ctitle);
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
		TeachAssistElementPeriodChildTitle ctitle = repo.get(id);
		if (null != ctitle) {
			ctitle.setSequence(sequence);
			ctitle.setUpdateAt(new Date());
			ctitle.setUpdateId(userId);
			repo.save(ctitle);
		}
	}

	@Override
	public List<TeachAssistElementPeriodChildTitle> get(long catalogId) {
		return repo.find("$resconFindByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	public AbstractTeachAssistElement findOne(long id) {
		return repo.get(id);
	}

	@Override
	public void copy(long newCatalogId, Collection<Long> ids, long userId) {
		Date now = new Date();
		List<TeachAssistElementPeriodChildTitle> titles = repo.mgetList(ids);
		for (TeachAssistElementPeriodChildTitle t : titles) {
			TeachAssistElementPeriodChildTitle newTitle = new TeachAssistElementPeriodChildTitle();
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
		return TeachAssistElementType.PERIOD_CHILD_TITLE;
	}
}
