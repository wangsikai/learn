package com.lanking.uxb.rescon.teach.api.impl.handler;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementReview;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 回顾反思模块handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementReviewHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementReviewRepo")
	private Repo<TeachAssistElementReview, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		TeachAssistElementReview review = null;
		if (form.getId() != null) {
			review = repo.get(form.getId());
		}
		if (null == review) {
			review = new TeachAssistElementReview();
			review.setCreateAt(new Date());
			review.setCreateId(form.getUserId());
			review.setType(getType());
			review.setSequence(form.getSequence());
			review.setTeachAssistCatalogId(form.getCatalogId());
			review.setUpdateAt(new Date());
			review.setUpdateId(form.getUserId());

			repo.save(review);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(review.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			review.setUpdateAt(new Date());
			review.setUpdateId(form.getUserId());

			repo.save(review);
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
		TeachAssistElementReview review = repo.get(id);
		if (null != review) {
			review.setSequence(sequence);
			review.setUpdateAt(new Date());
			review.setUpdateId(userId);

			repo.save(review);
		}
	}

	@Override
	public List<TeachAssistElementReview> get(long catalogId) {
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
		List<TeachAssistElementReview> rs = repo.mgetList(ids);
		for (TeachAssistElementReview r : rs) {
			TeachAssistElementReview n = new TeachAssistElementReview();
			n.setCreateAt(now);
			n.setCreateId(userId);
			n.setSequence(r.getSequence());
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
		}
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.REVIEW;
	}
}
