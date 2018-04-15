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
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.index.convert.StudentFallibleQuestionIndexConvert;
import com.lanking.uxb.service.index.value.StudentFallibleQuestionDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 学生错题索引
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class StudentFallibleQuestionIndexHandle extends AbstractIndexHandle {

	@Autowired
	@Qualifier("StudentFallibleQuestionRepo")
	Repo<StudentFallibleQuestion, Long> studentFallibleQuestionRepo;

	@Autowired
	private StudentFallibleQuestionIndexConvert studentFallibleQuestionIndexConvert;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.STUDENT_FALLIBLE_QUESTION == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.STUDENT_FALLIBLE_QUESTION;
	}

	@Override
	public String getDescription() {
		return "学生错题索引";
	}

	@Override
	public long dataCount() {
		return studentFallibleQuestionRepo.find("$indexDataCount").count();
	}

	@Override
	public void add(Long id) {
		StudentFallibleQuestion tfq = studentFallibleQuestionRepo.get(id);
		putDocument(convert(studentFallibleQuestionIndexConvert.to(tfq)));
	}

	@Override
	public void add(Collection<Long> ids) {
		List<StudentFallibleQuestion> tfqs = studentFallibleQuestionRepo.find("$indexmget", Params.param("ids", ids))
				.list();
		putDocuments(convert(studentFallibleQuestionIndexConvert.to(tfqs)));
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.STUDENT_FALLIBLE_QUESTION, id.toString());
	}

	@Override
	public void reindex() {
		int page = 1;
		reindexHandle(page, null);
	}

	public void reindexHandle(int page, Integer endPage) {
		Page<StudentFallibleQuestion> p = studentFallibleQuestionRepo.find("$indexQueryByPage")
				.fetch(P.index(page, PAGE_SIZE));
		while (p.isNotEmpty()) {
			putDocuments(convert(studentFallibleQuestionIndexConvert.to(p.getItems())));
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			if (page % 50 == 0) {
				studentFallibleQuestionRepo.clear();
			}
			p = studentFallibleQuestionRepo.find("$indexQueryByPage").fetch(P.index(page, PAGE_SIZE));
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(StudentFallibleQuestionDoc doc) {
		return new Document(IndexType.STUDENT_FALLIBLE_QUESTION.toString(), String.valueOf(doc.getId()),
				doc.documentValue());
	}

	private List<Document> convert(List<StudentFallibleQuestionDoc> docs) {
		List<Document> documents = Lists.newArrayList();
		for (StudentFallibleQuestionDoc doc : docs) {
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
