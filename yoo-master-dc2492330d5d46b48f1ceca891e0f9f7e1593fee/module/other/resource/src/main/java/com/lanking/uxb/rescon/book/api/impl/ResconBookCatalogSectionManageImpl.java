package com.lanking.uxb.rescon.book.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookCatalogSection;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogSectionManage;

/**
 * 书本目录章节对应关系服务接口实现.
 * 
 * @author wlche
 *
 */
@Service
@Transactional(readOnly = true)
public class ResconBookCatalogSectionManageImpl implements ResconBookCatalogSectionManage {
	@Autowired
	@Qualifier("BookCatalogSectionRepo")
	Repo<BookCatalogSection, Long> bookCatalogSectionRepo;

	@Override
	public List<BookCatalogSection> findByBookVersion(long bookVersionId) {
		return bookCatalogSectionRepo.find("$findByBookVersion", Params.param("bookVersionId", bookVersionId)).list();
	}

	@Override
	public List<BookCatalogSection> findByCatalog(long bookVersionId, long catalogId) {
		return bookCatalogSectionRepo
				.find("$findByCatalog", Params.param("bookVersionId", bookVersionId).put("catalogId", catalogId))
				.list();
	}

	@Override
	@Transactional
	public void save(Long bookVersionId, Long bookCatalogId, Integer textbookCode, Long sectionCode, long createId,
			int sequence) {
		BookCatalogSection bookCatalogSection = new BookCatalogSection();
		bookCatalogSection.setBookCatalogId(bookCatalogId);
		bookCatalogSection.setBookVersionId(bookVersionId);
		bookCatalogSection.setCreateAt(new Date());
		bookCatalogSection.setSectionCode(sectionCode);
		bookCatalogSection.setCreateId(createId);
		bookCatalogSection.setSequence(sequence);
		bookCatalogSection.setTextbookCode(textbookCode);

		// 更新次序
		bookCatalogSectionRepo.execute("$updateSequence",
				Params.param("textbookCode", textbookCode).put("sectionCode", sectionCode).put("sequence", sequence));

		bookCatalogSectionRepo.save(bookCatalogSection);
	}

	@Override
	@Transactional
	public void deleteCatalogRelation(long bookVersionId, long bookCatalogId) {
		bookCatalogSectionRepo.execute("$deleteCatalogRelation",
				Params.param("bookVersionId", bookVersionId).put("catalogId", bookCatalogId));
	}

	@Override
	@Transactional
	public void deleteCatalogRelation(long bookVersionId, Collection<Long> bookCatalogIds) {
		if (CollectionUtils.isNotEmpty(bookCatalogIds)) {
			bookCatalogSectionRepo.execute("$deleteCatalogsRelation",
					Params.param("bookVersionId", bookVersionId).put("catalogIds", bookCatalogIds));
		}
	}

	@Override
	@Transactional
	public void deleteCatalogRelationBySection(int textbookCode, long sectionCode) {
		bookCatalogSectionRepo.execute("$deleteCatalogRelationBySection",
				Params.param("textbookCode", textbookCode).put("sectionCode", sectionCode));
	}

	@Override
	@Transactional
	public void deleteCatalogRelationByBookVersion(long bookVersionId) {
		bookCatalogSectionRepo.execute("$deleteCatalogRelationByBookVersion",
				Params.param("bookVersionId", bookVersionId));
	}
}
