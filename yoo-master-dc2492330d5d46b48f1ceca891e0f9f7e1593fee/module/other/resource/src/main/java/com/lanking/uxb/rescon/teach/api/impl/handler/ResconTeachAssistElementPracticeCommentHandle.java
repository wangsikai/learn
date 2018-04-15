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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPracticeComment;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 训练评价模块
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPracticeCommentHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPracticeCommentRepo")
	private Repo<TeachAssistElementPracticeComment, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		TeachAssistElementPracticeComment comment = null;
		if (form.getId() != null) {
			comment = repo.get(form.getId());
		}
		if (null == comment) {
			comment = new TeachAssistElementPracticeComment();
			comment.setCreateAt(new Date());
			comment.setCreateId(form.getUserId());
			comment.setSequence(form.getSequence());
			comment.setTeachAssistCatalogId(form.getCatalogId());
			comment.setType(getType());

			comment.setUpdateAt(new Date());
			comment.setUpdateId(form.getUserId());

			repo.save(comment);

			TeachAssistCatalogElement ce = new TeachAssistCatalogElement();
			ce.setElementId(comment.getId());
			ce.setSequence(form.getSequence());
			ce.setTeachassistCatalogId(form.getCatalogId());
			ce.setType(getType());

			catalogElementRepo.save(ce);
		} else {
			comment.setUpdateAt(new Date());
			comment.setUpdateId(form.getUserId());

			repo.save(comment);
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
		TeachAssistElementPracticeComment comment = repo.get(id);
		if (null != comment) {
			comment.setUpdateAt(new Date());
			comment.setUpdateId(userId);
			comment.setSequence(sequence);

			repo.save(comment);
		}
	}

	@Override
	public List<TeachAssistElementPracticeComment> get(long catalogId) {
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
		List<TeachAssistElementPracticeComment> cs = repo.mgetList(ids);
		for (TeachAssistElementPracticeComment p : cs) {
			TeachAssistElementPracticeComment n = new TeachAssistElementPracticeComment();
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
		}
	}

	@Override
	public TeachAssistElementType getType() {
		return TeachAssistElementType.PRACTICE_COMMENT;
	}
}
