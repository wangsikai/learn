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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareComment;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.impl.ResconAbstractTeachAssistElementHandle;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

/**
 * 预习评价模块 handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistElementPrepareCommentHandle extends ResconAbstractTeachAssistElementHandle {
	@Autowired
	@Qualifier("TeachAssistElementPrepareCommentRepo")
	private Repo<TeachAssistElementPrepareComment, Long> repo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	@Transactional
	public void save(TeachAssistElementForm form) {
		TeachAssistElementPrepareComment comment = null;
		if (form.getId() != null) {
			comment = repo.get(form.getId());
		}
		if (comment == null) {
			comment = new TeachAssistElementPrepareComment();
			comment.setCreateAt(new Date());
			comment.setCreateId(form.getUserId());
			comment.setType(getType());
			comment.setSequence(form.getSequence());
			comment.setTeachAssistCatalogId(form.getCatalogId());

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
		TeachAssistElementPrepareComment comment = repo.get(id);
		if (null != comment) {
			comment.setSequence(sequence);
			comment.setUpdateAt(new Date());
			comment.setUpdateId(userId);

			repo.save(comment);
		}
	}

	@Override
	public List<TeachAssistElementPrepareComment> get(long catalogId) {
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
		List<TeachAssistElementPrepareComment> comments = repo.mgetList(ids);
		for (TeachAssistElementPrepareComment c : comments) {
			TeachAssistElementPrepareComment n = new TeachAssistElementPrepareComment();
			n.setCreateAt(now);
			n.setCreateId(userId);
			n.setSequence(c.getSequence());
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
		return TeachAssistElementType.PREPARE_COMMENT;
	}
}
