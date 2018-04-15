package com.lanking.uxb.service.index.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.index.convert.CollectionQuestionIndexConvert;
import com.lanking.uxb.service.index.value.CollectionQuestionDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 题目收藏索引
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class CollectionQuestionIndexHandle extends AbstractIndexHandle {

	@Autowired
	@Qualifier("QuestionCollectionRepo")
	Repo<QuestionCollection, Long> questionCollectionRepo;

	@Autowired
	private CollectionQuestionIndexConvert collectionQuestionIndexConvert;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.USER_QUESTION_COLLECT == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.USER_QUESTION_COLLECT;
	}

	@Override
	public String getDescription() {
		return "收藏索引";
	}

	@Override
	public long dataCount() {
		return questionCollectionRepo.find("$indexDataCount").count();
	}

	@Override
	public void add(Long id) {
		QuestionCollection tfq = questionCollectionRepo.get(id);
		putDocument(convert(collectionQuestionIndexConvert.to(tfq)));
	}

	@Override
	public void add(Collection<Long> ids) {
		List<QuestionCollection> tfqs = questionCollectionRepo.find("$indexmget", Params.param("ids", ids)).list();
		putDocuments(convert(collectionQuestionIndexConvert.to(tfqs)));
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.USER_QUESTION_COLLECT, id.toString());
	}

	@Override
	public void reindex() {
		int page = 1;
		reindexHandle(page, null);

	}

	public void reindexHandle(int page, Integer endPage) {
		Page<QuestionCollection> p = questionCollectionRepo.find("$indexQueryByPage").fetch(P.index(page, PAGE_SIZE));
		while (p.isNotEmpty()) {
			putDocuments(convert(collectionQuestionIndexConvert.to(p.getItems())));
			// 如果已经到了结束页，结束重建索引
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			if (page % 50 == 0) {
				questionCollectionRepo.clear();
			}
			p = questionCollectionRepo.find("$indexQueryByPage").fetch(P.index(page, PAGE_SIZE));
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(CollectionQuestionDoc doc) {
		return new Document(IndexType.USER_QUESTION_COLLECT.toString(), String.valueOf(doc.getId()),
				doc.documentValue());
	}

	private List<Document> convert(List<CollectionQuestionDoc> docs) {
		List<Document> documents = Lists.newArrayList();
		for (CollectionQuestionDoc doc : docs) {
			documents.add(convert(doc));
		}
		return documents;
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
