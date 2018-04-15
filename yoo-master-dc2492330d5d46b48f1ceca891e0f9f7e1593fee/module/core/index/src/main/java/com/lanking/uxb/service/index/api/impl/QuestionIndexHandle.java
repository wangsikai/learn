package com.lanking.uxb.service.index.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.index.convert.QuestionIndexConvert;
import com.lanking.uxb.service.index.value.QuestionIndexDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 习题索引.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月27日
 */
@Service
@Transactional(readOnly = true)
public class QuestionIndexHandle extends AbstractIndexHandle {
	@Autowired
	private QuestionIndexConvert questionIndexConvert;

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.QUESTION == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.QUESTION;
	}

	@Override
	public String getDescription() {
		return "题目索引";
	}

	@Override
	public long dataCount() {
		return questionRepo.find("$indexDataCount").count();
	}

	@Override
	public void add(Long id) {
		putDocument(this.convert(questionIndexConvert.to(questionRepo.get(id))));
	}

	@Override
	public void add(Collection<Long> ids) {
		putDocuments(this.convert(questionIndexConvert.to(questionRepo.mgetList(ids))));
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.QUESTION, id.toString());
	}

	@Override
	public void delete(Collection<Long> bizIds) {
		for (Long bizId : bizIds) {
			this.delete(bizId);
		}
	}

	@Override
	public void reindex() {
		int page = 1;
		reindexHandle(page, null);
	}

	public void reindexHandle(int page, Integer endPage) {
		Page<Question> p = questionRepo.find("$indexQueryByPage", Params.param()).fetch(P.index(page, PAGE_SIZE));
		while (p.isNotEmpty()) {
			putDocuments(this.convert(questionIndexConvert.to(p.getItems())));
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			if (page % 50 == 0) {
				questionRepo.clear();
			}
			p = questionRepo.find("$indexQueryByPage", Params.param()).fetch(P.index(page, PAGE_SIZE));
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(QuestionIndexDoc doc) {
		return new Document(IndexType.QUESTION.toString(), String.valueOf(doc.getResourceId()), doc.documentValue());
	}

	private List<Document> convert(List<QuestionIndexDoc> docs) {
		List<Document> list = new ArrayList<Document>(docs.size());
		for (QuestionIndexDoc questionIndexDoc : docs) {
			list.add(this.convert(questionIndexDoc));
		}
		return list;
	}

	@Override
	public void continueReindex(Integer startPage, Integer endPage) {
		int page = 1;
		if (startPage != null) {
			page = startPage;
		} else {
			long indexCount = this.indexCount();
			// 如果大于0说明之前已经跑过索引,判断接下来从哪页开始重建
			if (indexCount > 0) {
				page = (int) (indexCount / PAGE_SIZE + 1);
			}
		}
		reindexHandle(page, endPage);
	}
}
