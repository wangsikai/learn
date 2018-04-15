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

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.cache.BookCatalogCacheService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.value.VBookCatalog;

@Transactional(readOnly = true)
@Service
public class ZyBookServiceImpl implements ZyBookService {

	@Autowired
	@Qualifier("BookQuestionRepo")
	Repo<BookQuestion, Long> bookQuestionRepo;
	@Autowired
	@Qualifier("BookVersionRepo")
	Repo<BookVersion, Long> bookVersionRepo;
	@Autowired
	@Qualifier("BookCatalogRepo")
	Repo<BookCatalog, Long> bookCateglogRepo;
	@Autowired
	@Qualifier("BookRepo")
	Repo<Book, Long> bookRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	private BookCatalogCacheService catalogCacheService;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;

	@Override
	public BookCatalog getBookCatalog(Long id) {
		return bookCateglogRepo.get(id);
	}

	@Override
	public Map<Long, Book> mget(Collection<Long> ids) {
		return bookRepo.mget(ids);
	}

	@Override
	public Map<Long, BookCatalog> mgetBookCatalogs(List<Long> catalogIds) {
		return bookCateglogRepo.mget(catalogIds);
	}

	@Override
	public BookVersion getBookVersion(Long id) {
		return bookVersionRepo.get(id);
	}

	@Override
	public List<BookCatalog> getBookCatalogs(long bookVersionId) {
		return bookCateglogRepo.find("$getBookCatalogs", Params.param("bookVersionId", bookVersionId)).list();
	}

	@Override
	public Page<BookVersion> getSchoolBook(long textCategoryCode, long textbookCode, long schoold, Pageable p) {
		return bookVersionRepo.find(
				"$getSchoolBook",
				Params.param("textCategoryCode", textCategoryCode).put("textbookCode", textbookCode)
						.put("schoolId", schoold)).fetch(p);
	}

	@Override
	public Long getSchoolBookCount(long textCategoryCode, long textbookCode, long schoold) {
		return bookVersionRepo.find(
				"$getSchoolBookCount",
				Params.param("textCategoryCode", textCategoryCode).put("textbookCode", textbookCode)
						.put("schoolId", schoold)).count();
	}

	@Override
	public Page<BookVersion> getFreeBook(Integer textCategoryCode, Integer textbookCode, Pageable index) {
		return bookVersionRepo.find("$getFreeBook",
				Params.param("textCategoryCode", textCategoryCode).put("textbookCode", textbookCode)).fetch(index);
	}

	@Override
	public Long getFreeBookCount(Integer textCategoryCode, Integer textbookCode) {
		return bookVersionRepo.find("$getFreeBookCount",
				Params.param("textCategoryCode", textCategoryCode).put("textbookCode", textbookCode)).count();
	}

	@Override
	public List<BookVersion> getUserBookList(Integer textCategoryCode, Integer textBookCode, long userId) {
		Params params = Params.param();
		params.put("categoryCode", textCategoryCode);
		params.put("userId", userId);
		if (textBookCode != null) {
			params.put("textBookCode", textBookCode);
		}
		return bookVersionRepo.find("$getUserBookList", params).list();
	}

	@Override
	public List<BookVersion> getUserFreeBookList(Integer textCategoryCode, Integer textbookCode, long userId) {
		return bookVersionRepo.find("$getUserFreeBookList",
				Params.param("categoryCode", textCategoryCode).put("textBookCode", textbookCode).put("userId", userId))
				.list();
	}

	@Override
	public List<Long> listQuestions(Long bookCatalogId) {
		return bookQuestionRepo.find("$listQuestionsInCatalog", Params.param("bookCatalogId", bookCatalogId)).list(
				Long.class);

	}

	@Override
	public boolean existBook(int categoryCode, long userId, long id) {
		List<BookVersion> bookVersions = bookVersionRepo.find("$existBook",
				Params.param("categoryCode", categoryCode).put("userId", userId).put("id", id)).list();

		return CollectionUtils.isNotEmpty(bookVersions);
	}

	@Override
	public Map<String, Object> getRecommendCatalogs(long bookVersionId, long nowCatalogId, long userId) {
		BookVersion bookVersion = getBookVersion(bookVersionId);
		if (bookVersion == null) {
			return null;
		}
		if (!existBook(bookVersion.getTextbookCategoryCode(), userId, bookVersionId)) {
			return null;
		}
		List<BookCatalog> levelOneCatalogs = getLevelOneCatalogs(bookVersionId);
		// 若只返回当前一层章节数据,不返回推荐列表数据,则表明当前教辅已经全部推荐完成.
		if (nowCatalogId == 0) {
			Map<String, Object> map = new HashMap<String, Object>(1);
			map.put("levelOneCatalog", levelOneCatalogs.get(levelOneCatalogs.size() - 1));

			return map;
		}
		List<Long> levelOneCatalogIds = new ArrayList<Long>(levelOneCatalogs.size());
		for (BookCatalog bookCatalog : levelOneCatalogs) {
			levelOneCatalogIds.add(bookCatalog.getId());
		}
		Map<Long, List<Long>> allCatalogIds = catalogCacheService.getCatalogs(bookVersionId, levelOneCatalogIds);
		if (allCatalogIds.size() == 0) {
			allCatalogIds = generateCacheList(bookVersionId);
		}
		List<Long> recommendIds = null;
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		for (Map.Entry<Long, List<Long>> entry : allCatalogIds.entrySet()) {
			Long levelOneId = entry.getKey();
			List<Long> catalogIds = entry.getValue();
			int pos = catalogIds.indexOf(nowCatalogId);
			if (pos < 0) {
				continue;
			}

			retMap.put("levelOneCatalog", bookCateglogRepo.get(levelOneId));
			recommendIds = catalogIds.subList(pos, pos + 4 > catalogIds.size() ? catalogIds.size() : pos + 4);
		}

		Map<Long, BookCatalog> catalogMap = bookCateglogRepo.mget(recommendIds);
		List<BookCatalog> catalogs = new ArrayList<BookCatalog>(catalogMap.size());
		for (Long recommendId : recommendIds) {
			catalogs.add(catalogMap.get(recommendId));
		}
		retMap.put("catalogs", catalogs);
		return retMap;
	}

	@Override
	public List<BookCatalog> getLevelOneCatalogs(long bookVersionId) {
		return bookCateglogRepo.find("$getLevelOne", Params.param("bookVersionId", bookVersionId)).list();
	}

	@Override
	public Long getNextCatalog(long bookVersionId, long nowCatalogId) {
		List<BookCatalog> levelOneCatalogs = getLevelOneCatalogs(bookVersionId);
		List<Long> levelOneCatalogIds = new ArrayList<Long>(levelOneCatalogs.size());
		for (BookCatalog bookCatalog : levelOneCatalogs) {
			levelOneCatalogIds.add(bookCatalog.getId());
		}
		Map<Long, List<Long>> allCatalogIds = catalogCacheService.getCatalogs(bookVersionId, levelOneCatalogIds);
		if (allCatalogIds.size() == 0) {
			allCatalogIds = generateCacheList(bookVersionId);
		}
		Long nextId = 0L;
		for (Map.Entry<Long, List<Long>> entry : allCatalogIds.entrySet()) {
			Long levelOneId = entry.getKey();
			List<Long> catalogIds = entry.getValue();
			int pos = catalogIds.indexOf(nowCatalogId);
			if (pos < 0) {
				continue;
			}

			if (pos + 1 == catalogIds.size()) {
				if (levelOneCatalogIds.indexOf(levelOneId) == levelOneCatalogIds.size() - 1) {
					break;
				} else {
					nextId = allCatalogIds.get(levelOneCatalogIds.get(levelOneCatalogIds.indexOf(levelOneId) + 1)).get(
							0);
				}
			} else {
				nextId = catalogIds.get(pos + 1);
			}

		}

		return nextId;
	}

	@Override
	public Book getBook(long id) {
		return bookRepo.get(id);
	}

	/**
	 * 处理现有章节的树的缓存数据
	 *
	 * @param bookVersionId
	 *            书本章节id
	 * @return {@link List}
	 */
	private Map<Long, List<Long>> generateCacheList(long bookVersionId) {
		List<BookCatalog> catalogs = getBookCatalogs(bookVersionId);
		List<VBookCatalog> vs = bookCatalogConvert.to(catalogs);
		vs = bookCatalogConvert.assemblySectionTree(vs);

		int i = 1;
		Map<Long, List<Long>> retMap = new HashMap<Long, List<Long>>(vs.size());
		for (VBookCatalog v : vs) {
			Long levelOneId = v.getCode();
			List<Long> ids = Lists.newArrayList();
			if (CollectionUtils.isEmpty(v.getChildren())) {
				catalogCacheService.push(bookVersionId, levelOneId, v.getCode(), i);
				ids.add(v.getCode());
				i++;
			} else {
				for (VBookCatalog v1 : v.getChildren()) {
					if (CollectionUtils.isEmpty(v1.getChildren())) {
						catalogCacheService.push(bookVersionId, levelOneId, v1.getCode(), i);
						ids.add(v1.getCode());
						i++;
					} else {
						for (VBookCatalog v2 : v1.getChildren()) {
							catalogCacheService.push(bookVersionId, levelOneId, v2.getCode(), i);
							ids.add(v2.getCode());
							i++;
						}
					}
				}
			}

			retMap.put(levelOneId, ids);
		}

		return retMap;
	}

	@Override
	public List<BookVersion> getSchoolBookByDifResource(Integer textCategoryCode, Integer textbookCode, Long schoolId,
			Integer resourceCategoryCode) {
		Params params = Params.param();
		params.put("textCategoryCode", textCategoryCode);
		params.put("textbookCode", textbookCode);
		params.put("schoolId", schoolId);
		if (resourceCategoryCode != null) {
			params.put("resourceCategoryCode", resourceCategoryCode);
		}

		return bookVersionRepo.find("$getSchoolBookResource", params).list();
	}

	@Override
	public List<BookVersion> getFreeBookByDifResource(Integer textCategoryCode, Integer textbookCode,
			Integer resourceCategoryCode) {
		Params params = Params.param();
		params.put("textCategoryCode", textCategoryCode);
		params.put("textbookCode", textbookCode);
		if (resourceCategoryCode != null) {
			params.put("resourceCategoryCode", resourceCategoryCode);
		}

		return bookVersionRepo.find("$getFreeBookResource", params).list();
	}

	@Override
	public Map<Integer, List<BookVersion>> getBooksMapByDifResource(Integer textCategoryCode, Integer textbookCode,
			Collection<Integer> resourceCategoryCodes) {
		Params params = Params.param();
		params.put("textCategoryCode", textCategoryCode);
		params.put("textbookCode", textbookCode);
		if (resourceCategoryCodes != null) {
			params.put("resourceCategoryCodes", resourceCategoryCodes);
		}
		List<BookVersion> list = bookVersionRepo.find("$getFreeBookResource", params).list();
		Map<Integer, List<BookVersion>> map = new HashMap<Integer, List<BookVersion>>();
		for (BookVersion bv : list) {
			if (map.get(bv.getResourceCategoryCode()) == null) {
				List<BookVersion> tempList = new ArrayList<BookVersion>();
				tempList.add(bv);
				map.put(bv.getResourceCategoryCode(), tempList);
			} else {
				map.get(bv.getResourceCategoryCode()).add(bv);
			}
		}
		return map;
	}
}
