package com.lanking.uxb.service.index.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.service.index.convert.QuestionSimilarBaseIndexConvert;
import com.lanking.uxb.service.index.value.QuestionSimilarBaseIndexDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 相似题检索使用的基本题目索引.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月18日
 */
@Service
public class QuestionSimilarBaseIndexHandle extends AbstractIndexHandle {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private QuestionSimilarBaseIndexConvert questionSimilarBaseIndexConvert;
	@Autowired
	private QuestionSimilarService questionSimilarService;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.QUESTION_SIMILAR_BASE == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.QUESTION_SIMILAR_BASE;
	}

	@Override
	public String getDescription() {
		return "相似题基本题目索引";
	}

	@Override
	public long dataCount() {
		return -1;
	}

	@Override
	public void add(Long id) {
		Question question = questionSimilarService.getQuestion(id);
		if (question.getStatus() != CheckStatus.DRAFT && question.getType() != Question.Type.COMPOSITE) {
			// 草稿题与复合题不参与
			putDocument(this.convert(questionSimilarBaseIndexConvert.to(question)));
		}
	}

	@Override
	public void add(Collection<Long> ids) {
		putDocuments(this.convert(questionSimilarBaseIndexConvert.to(questionSimilarService.mgetQuestionList(ids))));
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.QUESTION_SIMILAR_BASE, id.toString());
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
		Page<Question> p = questionSimilarService.querySimilarBaseQuestion(page, PAGE_SIZE);

		while (p.isNotEmpty()) {
			putDocuments(this.convert(questionSimilarBaseIndexConvert.to(p.getItems())));
			if (page == p.getPageCount()) {
				logger.info("[" + getDescription() + "] 索引重建完成，共" + p.getTotalCount() + "条");
			}
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			p = questionSimilarService.querySimilarBaseQuestion(page, PAGE_SIZE);
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private Document convert(QuestionSimilarBaseIndexDoc doc) {
		return new Document(IndexType.QUESTION_SIMILAR_BASE.toString(), String.valueOf(doc.getQuestionId()),
				doc.documentValue());
	}

	private List<Document> convert(List<QuestionSimilarBaseIndexDoc> docs) {
		List<Document> list = new ArrayList<Document>(docs.size());
		for (QuestionSimilarBaseIndexDoc questionSimilarBaseIndexDoc : docs) {
			list.add(this.convert(questionSimilarBaseIndexDoc));
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
