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
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.index.convert.ExamIndexConvert;
import com.lanking.uxb.service.index.value.ExamIndexDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 试卷 索引
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月23日 下午2:47:45
 */
@Service
@Transactional(readOnly = true)
public class ExamIndexHandle extends AbstractIndexHandle {

	@Autowired
	@Qualifier("ExamPaperRepo")
	Repo<ExamPaper, Long> examPaperRepo;
	@Autowired
	private ExamIndexConvert examIndexConvert;

	private static int page = 1;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.EXAM_PAPER == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.EXAM_PAPER;
	}

	@Override
	public String getDescription() {
		return "试卷索引";
	}

	@Override
	public long dataCount() {
		return examPaperRepo.find("$indexDataCount").count();
	}

	@Override
	public void add(Long id) {
		ExamPaper exam = examPaperRepo.get(id);
		putDocument(convert(examIndexConvert.to(exam)));
	}

	@Override
	public void add(Collection<Long> ids) {
		List<ExamPaper> epList = examPaperRepo.find("$indexmget", Params.param("ids", ids)).list();
		putDocuments(convert(examIndexConvert.to(epList)));
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.EXAM_PAPER, id.toString());
	}

	@Override
	public void reindex() {
		int page = 1;
		reindexHandle(page, null);
	}

	public void reindexHandle(int page, Integer endPage) {
		Page<ExamPaper> p = examPaperRepo.find("$indexQueryByPage").fetch(P.index(page, PAGE_SIZE));
		while (p.isNotEmpty()) {
			putDocuments(convert(examIndexConvert.to(p.getItems())));
			// 如果已经到了结束页，结束重建索引
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			if (page % 50 == 0) {
				examPaperRepo.clear();
			}
			p = examPaperRepo.find("$indexQueryByPage").fetch(P.index(page, PAGE_SIZE));
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(ExamIndexDoc doc) {
		return new Document(IndexType.EXAM_PAPER.toString(), String.valueOf(doc.getId()), doc.documentValue());
	}

	private List<Document> convert(List<ExamIndexDoc> docs) {
		List<Document> documents = Lists.newArrayList();
		for (ExamIndexDoc doc : docs) {
			documents.add(convert(doc));
		}
		return documents;
	}

	@Override
	public void continueReindex(Integer startPage, Integer endPage) {
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
