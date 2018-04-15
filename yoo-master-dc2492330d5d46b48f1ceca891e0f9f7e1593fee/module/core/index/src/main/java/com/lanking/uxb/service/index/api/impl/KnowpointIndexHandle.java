package com.lanking.uxb.service.index.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowKnow;
import com.lanking.cloud.domain.common.baseData.MetaKnowKnowKey;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.index.value.KnowpointIndexDoc;
import com.lanking.uxb.service.search.api.impl.AbstractIndexHandle;

/**
 * 知识点索引
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年3月30日
 */
@Service
@Transactional(readOnly = true)
public class KnowpointIndexHandle extends AbstractIndexHandle {

	@Autowired
	@Qualifier("MetaKnowpointRepo")
	Repo<MetaKnowpoint, Integer> metaKnowpointRepo;
	@Autowired
	@Qualifier("KnowpointRepo")
	Repo<Knowpoint, Integer> knowpointRepo;
	@Autowired
	@Qualifier("MetaKnowKnowRepo")
	Repo<MetaKnowKnow, MetaKnowKnowKey> metaKnowKnowRepo;

	@Override
	public boolean accept(IndexTypeable type) {
		return IndexType.KNOWPOINT == type;
	}

	@Override
	public IndexTypeable getType() {
		return IndexType.KNOWPOINT;
	}

	@Override
	public String getDescription() {
		return "知识点索引";
	}

	@Override
	public long dataCount() {
		return knowpointRepo.find("$indexDataCount").count();
	}

	@Override
	public void reindex() {
		List<Document> documents = Lists.newArrayList();
		List<MetaKnowpoint> metaKnowpoints = metaKnowpointRepo.getAll();
		for (MetaKnowpoint metaKnowpoint : metaKnowpoints) {
			KnowpointIndexDoc doc = convert(metaKnowpoint);
			Document document = new Document(IndexType.KNOWPOINT.toString(), String.valueOf(doc.getKnowpointCode()),
					doc.documentValue());
			documents.add(document);
		}
		List<Knowpoint> knowpoints = knowpointRepo.getAll();
		for (Knowpoint knowpoint : knowpoints) {
			KnowpointIndexDoc doc = convert(knowpoint);
			Document document = new Document(IndexType.KNOWPOINT.toString(), String.valueOf(doc.getKnowpointCode()),
					doc.documentValue());
			documents.add(document);
		}
		putDocuments(documents);
	}

	@Override
	public void deleteReindex() {
		deleteByType(getType());
		reindex();
	}

	private KnowpointIndexDoc convert(MetaKnowpoint metaKnowpoint) {
		KnowpointIndexDoc doc = new KnowpointIndexDoc();
		doc.setKnowpointCode(metaKnowpoint.getCode());
		doc.setMetaCodes(Lists.newArrayList(metaKnowpoint.getCode()));
		doc.setName(metaKnowpoint.getName());
		doc.setPhaseCode(metaKnowpoint.getPhaseCode());
		doc.setSubjectCode(metaKnowpoint.getSubjectCode());
		doc.setIsMeta(1);
		return doc;
	}

	private KnowpointIndexDoc convert(Knowpoint knowpoint) {
		KnowpointIndexDoc doc = new KnowpointIndexDoc();
		List<MetaKnowKnow> knowKnows = metaKnowKnowRepo
				.find("$indexFindMetaKnowKnow", Params.param("knowPointCode", knowpoint.getCode())).list();
		for (MetaKnowKnow metaKnowKnow : knowKnows) {
			doc.getMetaCodes().add(metaKnowKnow.getMetaCode());
		}
		doc.setKnowpointCode(knowpoint.getCode());
		doc.setName(knowpoint.getName());
		doc.setPhaseCode(knowpoint.getPhaseCode());
		doc.setSubjectCode(knowpoint.getSubjectCode());
		doc.setIsMeta(0);
		return doc;
	}

	@Override
	public void continueReindex(Integer startPage, Integer endPage) {
		reindex();
	}
}
