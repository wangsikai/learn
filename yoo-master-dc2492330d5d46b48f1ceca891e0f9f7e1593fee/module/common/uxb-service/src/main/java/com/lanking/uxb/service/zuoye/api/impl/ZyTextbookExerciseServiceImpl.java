package com.lanking.uxb.service.zuoye.api.impl;

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
import com.lanking.uxb.service.zuoye.api.ZyTextbookExerciseService;

@Transactional(readOnly = true)
@Service
public class ZyTextbookExerciseServiceImpl implements ZyTextbookExerciseService {

	@Autowired
	@Qualifier("TextbookExerciseRepo")
	Repo<TextbookExercise, Long> tbeRepo;
	@Autowired
	@Qualifier("TextbookExerciseQuestionRepo")
	Repo<TextbookExerciseQuestion, Long> tbeqRepo;

	@Override
	public List<TextbookExercise> findByTextbook(int textbookCode) {
		return tbeRepo.find("$zyFindByTextbook", Params.param("textbookCode", textbookCode)).list();
	}

	@Override
	public List<TextbookExerciseQuestion> listQuestions(long textbookExerciseId) {
		return tbeqRepo.find("$zyListQuestions", Params.param("textbookExerciseId", textbookExerciseId)).list();
	}

	@Override
	public TextbookExercise get(long id) {
		return tbeRepo.get(id);
	}

	@Override
	public List<TextbookExercise> getTbeListBySectioCode(Long sectionCode, TextbookExerciseType type) {
		return tbeRepo
				.find("$getTbeListBySectioCode", Params.param("sectionCode", sectionCode).put("type", type.getValue()))
				.list();
	}

	@Transactional
	@Override
	public TextbookExercise create(Integer textbookCode, Long sectionCode, long userId, String name, boolean isDefalut,
			List<Long> qIds) {
		TextbookExercise tbe = new TextbookExercise();
		tbe.setTextbookCode(textbookCode);
		tbe.setSectionCode(sectionCode);
		tbe.setCreateId(userId);
		tbe.setName(name);
		tbe.setUpdateAt(new Date());
		tbe.setUpdateId(userId);
		// 除默认的，其他初始值为禁用
		tbe.setStatus(isDefalut == true ? Status.ENABLED : Status.DISABLED);
		tbe.setType(isDefalut == true ? TextbookExerciseType.DEFAULT : TextbookExerciseType.TEACHING_COACH);
		// 自动生成
		tbe.setAutoGenerate(true);
		tbe = tbeRepo.save(tbe);
		// 自动预置习题
		List<TextbookExerciseQuestion> tbeList = new ArrayList<TextbookExerciseQuestion>();
		for (int i = 0; i < qIds.size(); i++) {
			TextbookExerciseQuestion tbeq = new TextbookExerciseQuestion();
			tbeq.setExerciseId(tbe.getId());
			tbeq.setQuestionId(qIds.get(i));
			tbeq.setSequence(i + 1);
			tbeList.add(tbeq);
		}
		tbeqRepo.save(tbeList);
		return tbe;
	}

}
