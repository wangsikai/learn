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
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssist;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.index.convert.TeachAssistIndexConvert;
import com.lanking.uxb.service.index.value.TeachAssistIndexDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 教辅索引处理handle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class TeachAssistsIndexHandle extends AbstractIndexHandle {
	@Autowired
	private TeachAssistIndexConvert teachAssistIndexConvert;

	@Autowired
	@Qualifier("TeachAssistRepo")
	Repo<TeachAssist, Long> teachAssistRepo;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.TEACH_ASSIST == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.TEACH_ASSIST;
	}

	@Override
	public long dataCount() {
		return teachAssistRepo.find("$indexDataCount").count();
	}

	@Override
	public void add(Long id) {
		TeachAssist teachAssist = teachAssistRepo.get(id);
		if (null != teachAssist) {
			TeachAssistIndexDoc doc = teachAssistIndexConvert.to(teachAssist);
			putDocument(this.convert(doc));
		}
	}

	@Override
	public void add(Collection<Long> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			List<TeachAssist> assists = teachAssistRepo.mgetList(ids);
			List<TeachAssistIndexDoc> docs = teachAssistIndexConvert.to(assists);
			putDocuments(this.convert(docs));
		}
	}

	@Override
	public void delete(Long id) {
		deleteDocument(IndexType.TEACH_ASSIST, id.toString());
	}

	@Override
	public void reindex() {
		int page = 1;
		reindexHandle(page, null);
	}

	public void reindexHandle(int page, Integer endPage) {
		Page<Long> retPage = teachAssistRepo.find("$indexFind", Params.param()).fetch(P.index(page, PAGE_SIZE),
				Long.class);
		while (retPage.isNotEmpty()) {
			this.add(retPage.getItems());
			if (endPage != null && page == endPage) {
				return;
			}
			page++;
			if (page % 50 == 0) {
				teachAssistRepo.clear();
			}
			retPage = teachAssistRepo.find("$indexFind", Params.param()).fetch(P.index(page, PAGE_SIZE), Long.class);
		}
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	@Override
	public String getDescription() {
		return "教辅索引";
	}

	private Document convert(TeachAssistIndexDoc doc) {
		return new Document(IndexType.TEACH_ASSIST.toString(), String.valueOf(doc.getId()), doc.documentValue());
	}

	private List<Document> convert(List<TeachAssistIndexDoc> docs) {
		List<Document> list = new ArrayList<Document>(docs.size());
		for (TeachAssistIndexDoc doc : docs) {
			list.add(this.convert(doc));
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
