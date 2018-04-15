package com.lanking.uxb.zycon.base.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseQuestion;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.base.api.ZycTextbookExerciseService;

@Transactional(readOnly = true)
@Service
public class ZycTextbookExerciseServiceImpl implements ZycTextbookExerciseService {

	@Autowired
	@Qualifier("TextbookExerciseRepo")
	Repo<TextbookExercise, Long> tbeRepo;

	@Autowired
	@Qualifier("TextbookExerciseQuestionRepo")
	Repo<TextbookExerciseQuestion, Long> tbeqRepo;

	@Transactional
	@Override
	public TextbookExercise add(Integer textBookCode, Long sectionCode, Long uid, String name, Boolean isDefaultExercise) {
		TextbookExercise tbe = new TextbookExercise();
		tbe.setTextbookCode(textBookCode);
		tbe.setSectionCode(sectionCode);
		tbe.setCreateId(uid);
		tbe.setName(name);
		tbe.setUpdateAt(new Date());
		tbe.setUpdateId(uid);
		// 除默认的，其他初始值为禁用
		tbe.setStatus(isDefaultExercise == true ? Status.ENABLED : Status.DISABLED);
		tbe.setType(isDefaultExercise == true ? TextbookExerciseType.DEFAULT : TextbookExerciseType.TEACHING_COACH);
		tbe.setAutoGenerate(false);
		return tbeRepo.save(tbe);
	}

	@Override
	public List<TextbookExercise> findByTextbook(int textbookCode) {
		return tbeRepo.find("$zycFindByTextbook", Params.param("textbookCode", textbookCode)).list();
	}

	@Override
	public List<TextbookExerciseQuestion> listQuestions(long textbookExerciseId) {
		return tbeqRepo.find("$zycListQuestions", Params.param("textbookExerciseId", textbookExerciseId)).list();
	}

	@Override
	public TextbookExercise get(long id) {
		return tbeRepo.get(id);
	}

	@Transactional
	@Override
	public void setQuestions(List<Long> qIds, Long exerciseId) {
		// 清除原来属于该exerciese的题目
		tbeqRepo.execute("$clearExercise", Params.param("exerciseId", exerciseId));
		List<TextbookExerciseQuestion> tbeList = new ArrayList<TextbookExerciseQuestion>();
		for (int i = 0; i < qIds.size(); i++) {
			TextbookExerciseQuestion tbe = new TextbookExerciseQuestion();
			tbe.setExerciseId(exerciseId);
			tbe.setQuestionId(qIds.get(i));
			tbe.setSequence(i + 1);
			tbeList.add(tbe);
		}
		tbeqRepo.save(tbeList);
	}

	@Transactional
	@Override
	public void operateByStaus(long textbookExerciseId, Status status) {
		TextbookExercise te = get(textbookExerciseId);
		te.setStatus(status);
		tbeRepo.save(te);
	}

	@Transactional
	@Override
	public TextbookExercise updateExercise(long textbookExerciseId, String name) {
		TextbookExercise te = get(textbookExerciseId);
		te.setAutoGenerate(false);
		te.setUpdateAt(new Date());
		if (name != null) {
			te.setName(name);
		}
		tbeRepo.save(te);
		return te;
	}

	@Override
	public Long getExerciseNameCount(Long sectionCode, String name) {
		return tbeRepo.find("$getExerciseNameCount", Params.param("sectionCode", sectionCode).put("name", name))
				.count();
	}
}
