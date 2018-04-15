package com.lanking.uxb.service.question.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.question.api.QuestionService;

@Transactional(readOnly = true)
@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	@Qualifier("QuestionRepo")
	private Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("BookQuestionRepo")
	private Repo<BookQuestion, Long> bookQuestionRepo;

	@Override
	public Question get(long id) {
		return questionRepo.get(id);
	}

	@Override
	public Map<Long, Question> mget(Collection<Long> ids) {
		return questionRepo.mget(ids);
	}

	@Override
	public List<Question> mgetList(Collection<Long> ids) {
		return questionRepo.mgetList(ids);
	}

	@Override
	public List<Question> getSubQuestions(long id) {
		return questionRepo.find("$getSubQuestions", Params.param("parentId", id)).list();
	}

	@Override
	public Map<Long, List<Question>> mgetSubQuestions(Collection<Long> ids) {
		Map<Long, List<Question>> map = new HashMap<Long, List<Question>>(ids.size());
		for (Long id : ids) {
			map.put(id, new ArrayList<Question>());
		}
		List<Question> list = questionRepo.find("$getSubQuestions", Params.param("parentIds", ids)).list();
		for (Question question : list) {
			map.get(question.getParentId()).add(question);
		}
		return map;
	}

	@Override
	public Question findByCode(String code) {
		return questionRepo.find("$zyFindByCode", Params.param("code", code)).get();
	}

	@Override
	public int calPredictTime(Question.Type type, double difficulty, int subjectCode) {
		int predictTime = 0;
		// @since 教师端v1.3.0 0-0.5 改成0-0.4
		switch (type) {
		case SINGLE_CHOICE:
			if (difficulty >= 0.8 && difficulty <= 1) {
				predictTime += 1;
			} else if (difficulty >= 0.4 && difficulty < 0.8) {
				predictTime += 3;
			} else {
				predictTime += 5;
			}
			break;
		case QUESTION_ANSWERING:
			if (subjectCode == 202) {
				if (difficulty >= 0.8 && difficulty <= 1) {
					predictTime += 3;
				} else if (difficulty >= 0.4 && difficulty < 0.8) {
					predictTime += 5;
				} else {
					predictTime += 10;
				}
			} else if (subjectCode == 302) {
				if (difficulty >= 0.8 && difficulty <= 1) {
					predictTime += 5;
				} else if (difficulty >= 0.4 && difficulty < 0.8) {
					predictTime += 8;
				} else {
					predictTime += 10;
				}
			}
			break;
		case FILL_BLANK:
			if (subjectCode == 202) {
				if (difficulty >= 0.8 && difficulty <= 1) {
					predictTime += 1;
				} else if (difficulty >= 0.4 && difficulty < 0.8) {
					predictTime += 3;
				} else {
					predictTime += 5;
				}
			} else if (subjectCode == 302) {
				if (difficulty >= 0.8 && difficulty <= 1) {
					predictTime += 3;
				} else if (difficulty >= 0.4 && difficulty < 0.8) {
					predictTime += 5;
				} else {
					predictTime += 7;
				}
			}
			break;
		}
		return predictTime;
	}

	@Override
	public int calPredictTime(Question q) {
		return this.calPredictTime(q.getType(), q.getDifficulty(), q.getSubjectCode());
	}

	@Override
	public CursorPage<Long, BookQuestion> queryQuestionByCatalog(Long bookCatalogId, CursorPageable<Long> pageable,
			Type questionType, Double diff1, Double diff2) {
		Params params = Params.param();
		params.put("bookCatalogId", bookCatalogId);
		// 过滤复合题，不展示
		params.put("filterType", Question.Type.COMPOSITE.getValue());
		if (questionType != null) {
			params.put("questionType", questionType.getValue());
		}
		if (diff1 != null) {
			params.put("diff1", diff1);
		}
		if (diff2 != null) {
			params.put("diff2", diff2);
		}
		return bookQuestionRepo.find("$mQueryQuestionByCatalog", params).fetch(pageable);
	}
}
