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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficult;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficultExample;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentFallibleDifficultService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentService;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentFallibleDifficultForm;

@Transactional(readOnly = true)
@Service
public class ResconTeachAssistPresetContentFallibleDifficultServiceImpl implements
		ResconTeachAssistPresetContentFallibleDifficultService {
	@Autowired
	@Qualifier("TeachAssistPresetContentFallibleDifficultRepo")
	private Repo<TeachAssistPresetContentFallibleDifficult, Long> repo;
	@Autowired
	@Qualifier("TeachAssistPresetContentFallibleDifficultExampleRepo")
	private Repo<TeachAssistPresetContentFallibleDifficultExample, Long> exampleRepo;
	@Autowired
	@Qualifier("TeachAssistPresetContentRepo")
	private Repo<TeachAssistPresetContent, Long> contentRepo;
	@Autowired
	private ResconTeachAssistPresetContentService contentService;

	@Override
	public List<TeachAssistPresetContentFallibleDifficult> getByPresetId(Long teachassistPresetcontentId) {
		return repo.find("$getByPresetId", Params.param("teachassistPresetcontentId", teachassistPresetcontentId))
				.list();
	}

	@Transactional
	@Override
	public void check(Long id, CardStatus status) {
		TeachAssistPresetContentFallibleDifficult t = repo.get(id);
		t.setCheckStatus(status);
		repo.save(t);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		TeachAssistPresetContentFallibleDifficult t = repo.get(id);
		t.setDelStatus(Status.DELETED);
		repo.save(t);
	}

	@Transactional
	@Override
	public void save(TeachAssistPresetContentFallibleDifficultForm form) {
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
		// 保存易错点
		TeachAssistPresetContentFallibleDifficult t = null;
		if (form.getId() != null) {
			t = repo.get(form.getId());
			t.setUpdateId(form.getUserId());
			t.setUpdateAt(new Date());
		} else {
			t = new TeachAssistPresetContentFallibleDifficult();
			t.setCreateAt(new Date());
			t.setCreateId(form.getUserId());
		}
		// 保存状态变为草稿,提交变为未校验
		t.setCheckStatus(form.getStatus());
		t.setAnalysis(form.getAnalysis());
		t.setName(form.getName());
		t.setTargetedTrainingQuestions(form.getTargetedTrainingQuestions());
		t.setTeachassistPresetcontentId(form.getTeachassistPresetcontentId());
		repo.save(t);
		// 保存易错点例题相关表
		TeachAssistPresetContentFallibleDifficultExample example = null;
		if (form.getExampleId() != null) {
			example = exampleRepo.get(form.getExampleId());
			example.setUpdateId(form.getUserId());
			example.setUpdateAt(new Date());
		} else {
			example = new TeachAssistPresetContentFallibleDifficultExample();
			example.setCreateAt(new Date());
			example.setCreateId(form.getUserId());
		}
		example.setQuestionId(form.getQuestionId());
		example.setSolvingStrategy(form.getSolvingStrategy());
		example.setWrongAnswer(form.getWrongAnswer());
		example.setWrongAnalysis(form.getWrongAnalysis());
		example.setTeachassistPcFallibleDifficultId(t.getId());
		exampleRepo.save(example);
	}

	@Override
	public TeachAssistPresetContentFallibleDifficult get(Long id) {
		return repo.get(id);
	}

	@Override
	public List<TeachAssistPresetContentFallibleDifficult> getByCode(long code) {
		return repo.find("$findByKnowledgeCode", Params.param("code", code)).list();
	}

	@Override
	public Long nopassCount(Long teachassistPresetcontentId) {
		return repo.find("$nopassCount", Params.param("teachassistPresetcontentId", teachassistPresetcontentId)).get(
				Long.class);
	}
}
