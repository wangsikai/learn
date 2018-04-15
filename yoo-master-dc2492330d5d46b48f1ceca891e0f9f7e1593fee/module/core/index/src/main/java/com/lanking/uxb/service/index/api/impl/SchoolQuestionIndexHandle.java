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
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.index.convert.SchoolQuestionIndexConvert;
import com.lanking.uxb.service.index.value.SchoolQuestionDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 学校题目索引
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class SchoolQuestionIndexHandle extends AbstractIndexHandle {

	@Autowired
	@Qualifier("SchoolQuestionRepo")
	Repo<SchoolQuestion, Long> schoolCollectionRepo;

	@Autowired
	private SchoolQuestionIndexConvert schoolQuestionIndexConvert;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.SCHOOL_QUESTION == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.SCHOOL_QUESTION;
	}

	@Override
	public String getDescription() {
		return "学校题目索引";
	}

	@Override
	public long dataCount() {
		return schoolCollectionRepo.find("$indexDataCount").count();
	}

	@Override
	public void add(Long id) {
		SchoolQuestion school = schoolCollectionRepo.get(id);
		putDocument(convert(schoolQuestionIndexConvert.to(school)));
	}

	@Override
	public void add(Collection<Long> ids) {
		List<SchoolQuestion> tfqs = schoolCollectionRepo.find("$indexmget", Params.param("ids", ids)).list();
		putDocuments(convert(schoolQuestionIndexConvert.to(tfqs)));
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.SCHOOL_QUESTION, id.toString());
	}

	@Override
	public void reindex() {
		int page = 1;
		reindexHandle(page, null);
	}

	public void reindexHandle(int page, Integer endPage) {
		Page<SchoolQuestion> p = schoolCollectionRepo.find("$indexQueryByPage").fetch(P.index(page, PAGE_SIZE));
		while (p.isNotEmpty()) {
			putDocuments(convert(schoolQuestionIndexConvert.to(p.getItems())));
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			if (page % 50 == 0) {
				schoolCollectionRepo.clear();
			}
			p = schoolCollectionRepo.find("$indexQueryByPage").fetch(P.index(page, PAGE_SIZE));
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(SchoolQuestionDoc doc) {
		return new Document(IndexType.SCHOOL_QUESTION.toString(), String.valueOf(doc.getId()), doc.documentValue());
	}

	private List<Document> convert(List<SchoolQuestionDoc> docs) {
		List<Document> documents = Lists.newArrayList();
		for (SchoolQuestionDoc doc : docs) {
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
