package com.lanking.uxb.rescon.teach.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentPreview;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentPreviewService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentService;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentPreviewForm;

@Transactional(readOnly = true)
@Service
public class ResconTeachAssistPresetContentPreviewServiceImpl implements ResconTeachAssistPresetContentPreviewService {
	@Autowired
	@Qualifier("TeachAssistPresetContentPreviewRepo")
	private Repo<TeachAssistPresetContentPreview, Long> repo;
	@Autowired
	@Qualifier("TeachAssistPresetContentRepo")
	private Repo<TeachAssistPresetContent, Long> contentRepo;
	@Autowired
	private ResconTeachAssistPresetContentService contentService;

	@Override
	public List<TeachAssistPresetContentPreview> getByPresetId(Long teachassistPresetcontentId) {
		return repo.find("$getByPresetId", Params.param("teachassistPresetcontentId", teachassistPresetcontentId))
				.list();
	}

	@Transactional
	@Override
	public void delete(Long id) {
		TeachAssistPresetContentPreview t = repo.get(id);
		t.setDelStatus(Status.DELETED);
		repo.save(t);

	}

	@Transactional
	@Override
	public void check(Long id, CardStatus status) {
		TeachAssistPresetContentPreview t = repo.get(id);
		t.setCheckStatus(status);
		repo.save(t);
	}

	@Transactional
	@Override
	public void save(TeachAssistPresetContentPreviewForm form) {
		// 说明教辅预置内容没有保存，这里特殊处理：学习目标，解题方法存空
		TeachAssistPresetContent content = contentService.getByKsCode(form.getKnowledgeSystemCode());
		if (content == null) {
			content = new TeachAssistPresetContent();
			content.setCreateAt(new Date());
			content.setCreateId(form.getUserId());
			content.setKnowledgeSystemCode(form.getKnowledgeSystemCode());
			contentRepo.save(content);
		}
		form.setTeachassistPresetcontentId(content.getId());
		TeachAssistPresetContentPreview t = null;
		if (form.getId() != null) {
			t = repo.get(form.getId());
			t.setUpdateAt(new Date());
			t.setUpdateId(form.getUserId());
		} else {
			t = new TeachAssistPresetContentPreview();
			t.setCreateAt(new Date());
			t.setCreateId(form.getUserId());
		}
		// 保存状态变为草稿,提交变为未校验
		t.setCheckStatus(form.getStatus());
		t.setKnowpoints(form.getKnowpoints());
		t.setName(form.getName());
		t.setPreviewQuestions(form.getPreviewQuestions());
		t.setSelfTestQuestions(form.getSelfTestQuestions());
		t.setTeachassistPresetcontentId(form.getTeachassistPresetcontentId());
		repo.save(t);
	}

	@Override
	public TeachAssistPresetContentPreview get(Long id) {
		return repo.get(id);
	}

	@Override
	public List<TeachAssistPresetContentPreview> findByKnowledgeSystem(long code) {
		return repo.find("$resconFindByKnowledgeSystem", Params.param("code", code)).list();
	}

	@Override
	public Long nopassCount(Long teachassistPresetcontentId) {
		return repo.find("$nopassCount", Params.param("teachassistPresetcontentId", teachassistPresetcontentId)).get(
				Long.class);
	}
}
