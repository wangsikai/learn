package com.lanking.uxb.rescon.book.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionManage;
import com.lanking.uxb.rescon.book.form.BookQuestionQueryForm;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconSchoolQuestionManage;
import com.lanking.uxb.service.counter.api.impl.BookCatalogCounterProvider;
import com.lanking.uxb.service.counter.api.impl.BookCounterProvider;

/**
 * 书本习题接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月23日
 */
@Service
@Transactional(readOnly = true)
public class ResconBookQuestionManageImpl implements ResconBookQuestionManage {
	@Autowired
	@Qualifier("BookQuestionRepo")
	Repo<BookQuestion, Long> bookQuestionRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	private BookCounterProvider bookCounterProvider;
	@Autowired
	private BookCatalogCounterProvider bookCatalogCounterProvider;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconSchoolQuestionManage resconSchoolQuestionManage;

	@Override
	@Transactional
	public void addQuestion(long questionId, long bookVersionId, long bookCatalogId, long createId)
			throws ResourceConsoleException {
		long questionNum = bookQuestionRepo.find("$getQuestionNum", Params.param("bookCatalogId", bookCatalogId))
				.count();
		BookQuestion bookQuestion = new BookQuestion();
		bookQuestion.setBookCatalogId(bookCatalogId);
		bookQuestion.setBookVersionId(bookVersionId);
		bookQuestion.setQuestionId(questionId);
		bookQuestion.setCreateId(createId);
		bookQuestion.setSequence((int) questionNum + 1);
		bookQuestionRepo.save(bookQuestion);
	}

	@Override
	@Transactional
	public void addQuestion(BookQuestion bookQuestion) throws ResourceConsoleException {
		bookQuestionRepo.save(bookQuestion);
	}

	@Override
	@Transactional
	public void addQuestions(Collection<BookQuestion> bookQuestions) throws ResourceConsoleException {
		bookQuestionRepo.save(bookQuestions);
	}

	@Override
	@Transactional(readOnly = true)
	public BookQuestion getQuestionFromCatalog(long questionId, long bookCatalogId) {
		List<BookQuestion> list = bookQuestionRepo.find("$getQuestionFromCatalog",
				Params.param("bookCatalogId", bookCatalogId).put("questionId", questionId)).list();
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	@Transactional(readOnly = true)
	public BookQuestion getQuestionFromVersion(long questionId, long bookVersionId) {
		List<BookQuestion> list = bookQuestionRepo.find("$getQuestionFromCatalog",
				Params.param("bookVersionId", bookVersionId).put("questionId", questionId)).list();
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	@Transactional
	public void moveQuestion(long questionId, long bookCatalogId, int flag, long createId)
			throws ResourceConsoleException {
		BookQuestion bookQuestion = this.getQuestionFromCatalog(questionId, bookCatalogId);
		if (bookQuestion.getSequence() == 0 && flag == -1) {
			return;
		}
		List<BookQuestion> list = bookQuestionRepo
				.find("$getQuestionFromCatalog",
						Params.param("bookCatalogId", bookCatalogId).put("sequence", bookQuestion.getSequence() + flag))
				.list();
		if (list.size() > 0) {
			BookQuestion bookQuestion2 = list.get(0);
			int sequence2 = bookQuestion2.getSequence();
			bookQuestion.setSequence(sequence2);
			bookQuestion2.setSequence(bookQuestion.getSequence() + flag);
			bookQuestionRepo.save(bookQuestion);
			bookQuestionRepo.save(bookQuestion2);
		}
	}

	@Override
	@Transactional
	public void moveQuestionTo(long bookVersionId, long questionId, long bookCatalogId, long createId)
			throws ResourceConsoleException {
		List<BookQuestion> list = bookQuestionRepo.find("$getQuestionFromVersion",
				Params.param("bookVersionId", bookVersionId).put("questionId", questionId)).list();

		if (list.size() > 0) {
			BookQuestion bookQuestion = list.get(0);
			Long oldBookCatalogId = bookQuestion.getBookCatalogId();
			bookQuestion.setBookCatalogId(bookCatalogId);
			bookQuestion.setCreateAt(new Date());
			bookQuestion.setCreateId(createId);
			bookQuestionRepo.save(bookQuestion);

			if (oldBookCatalogId == null || oldBookCatalogId == 0) {
				bookCounterProvider.incrNoCatalogResourceCount(bookVersionId, -1);
				bookCatalogCounterProvider.incrResourceCount(bookCatalogId, 1);
			} else {
				bookCatalogCounterProvider.incrResourceCount(oldBookCatalogId, -1);
				bookCatalogCounterProvider.incrResourceCount(bookCatalogId, 1);
			}
		}
	}

	@Override
	@Transactional
	public void deleteQuestion(long questionId, long bookCatalogId, long createId) throws ResourceConsoleException {
		BookQuestion bookQuestion = this.getQuestionFromCatalog(questionId, bookCatalogId);
		int sequence = bookQuestion.getSequence();
		bookQuestionRepo.delete(bookQuestion);
		bookQuestionRepo.execute("$updateDeleteSequence",
				Params.param("bookCatalogId", bookCatalogId).put("sequence", sequence));
		Question question = questionRepo.get(questionId);

		// 只有已通过的题目进行校本题目计数
		if (question.getSchoolId() != 0 && question.getStatus() == CheckStatus.PASS) {
			questionManage.updateQuestionSchoolCount(question.getSchoolId(), -1);
			resconSchoolQuestionManage.delSchoolQuestion(question.getSchoolId(), questionId);
		}

		question.setSchoolId(0); // 从校本题库中删除
		questionRepo.save(question);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> hasQuestion(long bookVersionId, Collection<String> questionCodes) {
		List<String> codes = bookQuestionRepo
				.find("$findHasQuestionCode",
						Params.param("questionCodes", questionCodes).put("bookVersionId", bookVersionId))
				.list(String.class);
		return codes;
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getMaxSequence(long bookCatalogId) {
		Integer max = bookQuestionRepo.find("$getMaxSequence", Params.param("bookCatalogId", bookCatalogId))
				.get(Integer.class);
		return max == null ? 0 : max.intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Question> query(BookQuestionQueryForm form, Pageable pageable) {
		Params params = Params.param("bookVersionId", form.getBookVersionId());
		if (form.getCatalogIds() != null && form.getCatalogIds().size() > 0) {
			params.put("bookCatalogIds", form.getCatalogIds());
		}
		if (StringUtils.isNotBlank(form.getKey())) {
			params.put("keys", "%" + form.getKey() + "%");
		}
		if (StringUtils.isNotBlank(form.getCode())) {
			params.put("code", form.getCode());
		}
		if (null != form.getCheckStatus()) {
			params.put("checkStatus", form.getCheckStatus().getValue());
		}
		if (null != form.getQuestionType()) {
			params.put("questionType", form.getQuestionType().getValue());
		}
		if (null != form.getNotag()) {
			params.put("notag", form.getNotag());
		}
		if (null != form.getQuestionTags()) {
			params.put("questionTags", form.getQuestionTags());
		}
		if (null != form.getV3flag() && form.getV3flag() == 1) {
			params.put("v3flag", form.getV3flag());
		}
		if (null != form.getBookQuestionCategoryId()) {
			if (form.getBookQuestionCategoryId() == 0) {
				params.put("nobqc", 1);
			} else {
				params.put("bookQuestionCategoryId", form.getBookQuestionCategoryId());
			}
		}
		Page<Question> page = bookQuestionRepo.find("$query", params).fetch(pageable, Question.class);
		return page;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<CheckStatus, Integer> getBookCounts(long bookVersionId) {
		List<Map> countList = bookQuestionRepo.find("$getStausCounts", Params.param("bookVersionId", bookVersionId))
				.list(Map.class);
		Map<CheckStatus, Integer> map = new HashMap<CheckStatus, Integer>(5);
		for (Map countmap : countList) {
			Object count = countmap.get("count");
			map.put(CheckStatus.findByValue(Integer.parseInt(countmap.get("status").toString())),
					count == null ? 0 : Integer.parseInt(count.toString()));
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map<CheckStatus, Integer>> getBookCounts(Collection<Long> bookVersionIds) {
		if (CollectionUtils.isEmpty(bookVersionIds)) {
			return Lists.newArrayList();
		}
		List<Map> list = bookQuestionRepo
				.find("$getBookversionQuestionCounts", Params.param("bookVersionIds", bookVersionIds)).list(Map.class);
		List<Map<CheckStatus, Integer>> returnList = new ArrayList<Map<CheckStatus, Integer>>(list.size());
		for (long id : bookVersionIds) {
			Map<CheckStatus, Integer> map = new HashMap<CheckStatus, Integer>(5);
			for (int i = list.size() - 1; i >= 0; i--) {
				int count = Integer.parseInt(list.get(i).get("count").toString());
				long versionId = Long.parseLong(list.get(i).get("id").toString());
				int status = Integer.parseInt(list.get(i).get("status").toString());
				if (id == versionId) {
					map.put(CheckStatus.findByValue(status), count);
					list.remove(i);
				}
			}
			returnList.add(map);
		}
		return returnList;
	}

	@Override
	public Page<BookQuestion> query(long bookVersionId, Pageable pageable) {
		return bookQuestionRepo.find("select * from book_question where book_version_id=:bookVersionId order by id ASC",
				Params.param("bookVersionId", bookVersionId)).fetch(pageable);
	}

	@Override
	public List<BookQuestion> queryByQuestionId(long questionId) {
		return bookQuestionRepo.find("select * from book_question where question_id=:questionId",
				Params.param("questionId", questionId)).list();
	}

	@Override
	@Transactional
	public void changeQuestion(long bookVersionId, long oldQuestionId, long newQuestionId) {
		bookQuestionRepo.execute("$changeQuestion", Params.param("bookVersionId", bookVersionId)
				.put("oldQuestionId", oldQuestionId).put("newQuestionId", newQuestionId));
	}

	@Override
	public Integer countCatalog(long bookCatalogId) {
		Long count = bookQuestionRepo.find("$countCatalog", Params.param("bookCatalogId", bookCatalogId))
				.count();
		return count.intValue();
	}
}
