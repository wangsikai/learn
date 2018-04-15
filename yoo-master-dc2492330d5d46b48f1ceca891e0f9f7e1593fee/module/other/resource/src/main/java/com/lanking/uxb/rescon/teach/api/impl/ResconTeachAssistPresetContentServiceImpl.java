package com.lanking.uxb.rescon.teach.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContent;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentService;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentForm;

@Transactional(readOnly = true)
@Service
public class ResconTeachAssistPresetContentServiceImpl implements ResconTeachAssistPresetContentService {

	@Autowired
	@Qualifier("TeachAssistPresetContentRepo")
	private Repo<TeachAssistPresetContent, Long> repo;

	@Override
	public TeachAssistPresetContent getByKsCode(Long code) {
		return repo.find("$getByKsCode", Params.param("code", code)).get(TeachAssistPresetContent.class);
	}

	@Transactional
	@Override
	public void save(TeachAssistPresetContentForm form) {
		TeachAssistPresetContent t = this.getByKsCode(form.getKnowledgeSystemCode());
		if (t != null) {
			t.setUpdateAt(new Date());
			t.setUpdateId(form.getUserId());
		} else {
			t = new TeachAssistPresetContent();
			t.setCreateAt(new Date());
			t.setCreateId(form.getUserId());
		}
		t.setKnowledgeSystemCode(form.getKnowledgeSystemCode());
		if (form.getLearningGoals() != null) {
			t.setLearningGoals(form.getLearningGoals());
		}
		if (form.getSolvingMethod() != null) {
			t.setSolvingMethod(form.getSolvingMethod());
		}
		repo.save(t);
	}

	@Override
	public List<TeachAssistPresetContent> mgetByKsCodes(Collection<Long> codes) {
		return repo.find("$mgetByKsCodes", Params.param("codes", codes)).list();
	}
}
