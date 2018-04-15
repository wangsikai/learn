package com.lanking.uxb.service.zuoye.api.impl;

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
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyBookQuestionService;

@Transactional(readOnly = true)
@Service
public class ZyBookQuestionServiceImpl implements ZyBookQuestionService{

	@Autowired
	@Qualifier("BookQuestionRepo")
	Repo<BookQuestion, Long> bookQuestionRepo;
	
	@Override
	public List<Long> findQuestionByBookCategoryId(Long bookCategoryId) {
		return bookQuestionRepo.find("$findQuestionByBookCategoryId", Params.param("bookCategoryId", bookCategoryId)).list(Long.class);
	}

	@Override
	public Map<Long, List<Long>> findQuestionByBookCategroyIds(
			Collection<Long> bookCategoryIds,Type questionType, Double diff1, Double diff2) {
		Params params = Params.param("bookCategoryIds", bookCategoryIds);
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
		List<BookQuestion> list = bookQuestionRepo.find("$findQuestionByBookCategoryIds", params).list();
		Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
		for(BookQuestion b:list){
			if(map.get(b.getBookQuestionCatalogId()) == null){
				List<Long> tempList = new ArrayList<Long>();
				tempList.add(b.getQuestionId());
				map.put(b.getBookQuestionCatalogId(), tempList);
			}else{
				map.get(b.getBookQuestionCatalogId()).add(b.getQuestionId());
			}
		}
		return map;
	}

}
